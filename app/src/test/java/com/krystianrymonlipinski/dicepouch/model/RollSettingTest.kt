package com.krystianrymonlipinski.dicepouch.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RollSettingTest {

    private lateinit var testObj: RollSetting

    @Before
    fun setUp() {
        testObj = RollSetting(Die(6))
    }

    @Test
    fun changeDie() {
        val dieToChange = Die(20)
        testObj = testObj.changeDie(dieToChange)
        assertEquals(dieToChange, testObj.die)
    }

    @Test
    fun changeDiceNumber() {
        testObj = testObj.changeDiceNumber(change = 1)
        assertEquals(2, testObj.diceNumber)
    }

    @Test
    fun changeDiceNumber_checkConstraints() {
        testObj = testObj
            .copy(diceNumber = 30)
            .changeDiceNumber(change = 1)
        assertEquals(1, testObj.diceNumber)

        testObj = testObj
            .copy(diceNumber = 1)
            .changeDiceNumber(change = -1)
        assertEquals(30, testObj.diceNumber)
    }

    @Test
    fun changeModifier() {
        testObj = testObj.changeModifier(change = 1)
        assertEquals(1, testObj.modifier)
    }

    @Test
    fun changeModifier_checkConstraints() {
        testObj = testObj
            .copy(modifier = 30)
            .changeModifier(change = 1)
        assertEquals(-30, testObj.modifier)

        testObj = testObj
            .copy(modifier = -30)
            .changeModifier(change = -1)
        assertEquals(30, testObj.modifier)
    }

    @Test
    fun changeMechanic() {
        testObj = testObj.changeMechanic(RollSetting.Mechanic.ADVANTAGE)
        assertEquals(RollSetting.Mechanic.ADVANTAGE, testObj.mechanic)
    }

    @Test
    fun buildRollDescription_withOneDie() {
        val testObj = RollSetting(Die(6))
        assertEquals("1d6", testObj.rollDescription)
    }

    @Test
    fun buildRollDescription_withMultipleDice() {
        val testObj = RollSetting(Die(8), 3)
        assertEquals("3d8", testObj.rollDescription)
    }

    @Test
    fun buildRollDescription_withMultipleDice_withModifier() {
        val testObj = RollSetting(Die(10), 2, -1)
        assertEquals("2d10 - 1", testObj.rollDescription)
    }

    @Test
    fun buildRollDescription_withMultipleDice_withModifier_withAdvantage() {
        val testObj = RollSetting(Die(12), 4, 2, RollSetting.Mechanic.ADVANTAGE)
        assertEquals("4d12 + 2 (A)", testObj.rollDescription)
    }

    @Test
    fun buildRollDescription_withMultipleDice_withModifier_withDisadvantage() {
        val testObj = RollSetting(Die(20), 2, 5, RollSetting.Mechanic.DISADVANTAGE)
        assertEquals("2d20 + 5 (D)", testObj.rollDescription)
    }
}