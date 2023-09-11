package com.krystianrymonlipinski.dicepouch.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TryStateTest {

    private lateinit var testObj: TryState

    @Before
    fun setUp() {
        testObj = TryState(
            throws = listOf(3, 2, null),
            result = null,
            isChosen = false
        )
    }

    @Test
    fun updateWithNewThrow() {
        testObj = testObj.updateWithNewThrow(2, 10)
        assertEquals(listOf(3, 2, 10), testObj.throws)
    }

    @Test
    fun updateResult_whenAnyThrowIsNull() {
        testObj = testObj.updateResult(2)
        assertNull(testObj.result)
    }

    @Test
    fun updateResult_whenNoThrowIsNull() {
        testObj = testObj
            .copy(throws = listOf(3, 2, 7))
            .updateResult(-1)
        assertEquals(11, testObj.result)
    }

    @Test
    fun updateIsChosen() {
        testObj = testObj.updateAsChosen()
        assertTrue(testObj.isChosen)
    }
}