package com.krystianrymonlipinski.dicepouch.model

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DiceSetTest {

    private lateinit var testObj: DiceSet


    @Before
    fun setUp() {
        testObj = DiceSet()
    }

    @Test
    fun addNewDie() {
        val dieToAdd = Die(8)
        testObj = testObj.addNewDie(dieToAdd)

        assertEquals(1, testObj.dice.size)
        assertEquals(dieToAdd, testObj.dice[0])
    }

    @Test
    fun deleteDie() {
        val dieToRemain = Die(20)
        testObj = testObj
            .addNewDie(Die(10))
            .addNewDie(dieToRemain)
        testObj = testObj.deleteDie(0)

        assertEquals(1, testObj.dice.size)
        assertEquals(dieToRemain, testObj.dice[0])
    }

    @Test
    fun changeName() {
        testObj = testObj.changeName("new_name")
        assertEquals("new_name", testObj.name)
    }
}