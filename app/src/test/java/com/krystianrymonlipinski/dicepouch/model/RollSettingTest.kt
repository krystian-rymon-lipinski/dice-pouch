package com.krystianrymonlipinski.dicepouch.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RollSettingTest {

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