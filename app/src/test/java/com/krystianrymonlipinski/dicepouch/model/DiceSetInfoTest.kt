package com.krystianrymonlipinski.dicepouch.model

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DiceSetInfoTest {

    private lateinit var testObj: DiceSetInfo

    @Before
    fun setUp() {
        testObj = DiceSetInfo()
    }

    @Test
    fun changeName() {
        testObj = testObj.changeName("another name")
        assertEquals("another name", testObj.name)
    }

    @Test
    fun changeDiceColor() {
        testObj = testObj.changeDiceColor(Color.Blue)
        assertEquals(Color.Blue, testObj.diceColor)
    }

    @Test
    fun changeNumbersColor() {
        testObj = testObj.changeNumbersColor(Color.Magenta)
        assertEquals(Color.Magenta, testObj.numbersColor)
    }
}