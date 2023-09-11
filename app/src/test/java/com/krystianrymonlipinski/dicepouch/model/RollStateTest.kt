package com.krystianrymonlipinski.dicepouch.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class RollStateTest {

    private lateinit var testObj: RollState

    @Mock
    private lateinit var rollSetting: RollSetting


    @Before
    fun setUp() {
        whenever(rollSetting.numberOfTries).thenReturn(1)
        whenever(rollSetting.diceNumber).thenReturn(4)
        testObj = RollState(setting = rollSetting)
    }


    @Test
    fun updateTryWithNewThrow() {
        val tryStateToMatch = TryState(throws = listOf(2, null, null, null))
        testObj = testObj.updateTryWithNewThrow(2)
        assertEquals(tryStateToMatch, testObj.tries[0])
    }

    @Test
    fun updateTryWithNewThrow_whenOnSecondTry_whenOnNotFirstThrow() {
        val tryStateToMatch = TryState(throws = listOf(null, null, 5, null))
        testObj = testObj
            .copy(
                tries = listOf(
                    TryState(throws = listOf(null, null, null, null)),
                    TryState(throws = listOf(null, null, null, null))
                ),
                currentTry = 2,
                currentThrow = 3
            )
            .updateTryWithNewThrow(5)
        assertEquals(tryStateToMatch, testObj.tries[1])
    }

    @Test
    fun markNextThrow() {
        testObj = testObj.markNextThrow()
        assertEquals(2, testObj.currentThrow)
    }

    @Test
    fun updateTryWithResult() {
        val tryStateToMatch = TryState(throws = finishedThrows, result = 13)
        whenever(rollSetting.modifier).thenReturn(-1)
        testObj = testObj.copy(tries = listOf(testObj.tries[0].copy(throws = finishedThrows)))

        testObj = testObj.updateTryWithResult()
        assertEquals(tryStateToMatch, testObj.tries[0])
    }

    @Test
    fun markNextTry() {
        testObj = testObj.markNextTry()
        assertEquals(2, testObj.currentTry)
    }

    @Test
    fun updateTriesWithChosenOne_whenMechanicNormal() {
        whenever(rollSetting.mechanic).thenReturn(RollSetting.Mechanic.NORMAL)
        testObj = testObj
            .copy(tries = listOf(testObj.tries[0].copy(result = 13)))
            .updateTriesWithChosenOne()

        assertEquals(listOf(true), testObj.tries.map { it.isChosen })
    }

    @Test
    fun updateTriesWithChosenOne_whenMechanicAdvantage() {
        whenever(rollSetting.mechanic).thenReturn(RollSetting.Mechanic.ADVANTAGE)
        testObj = testObj
            .copy(tries = listOf(
                testObj.tries[0].copy(result = 13),
                testObj.tries[0].copy(result = 17)
            ))
            .updateTriesWithChosenOne()

        assertEquals(listOf(false, true), testObj.tries.map { it.isChosen })
    }

    @Test
    fun updateTriesWithChosenOne_whenMechanicDisadvantage() {
        whenever(rollSetting.mechanic).thenReturn(RollSetting.Mechanic.DISADVANTAGE)
        testObj = testObj
            .copy(tries = listOf(
                testObj.tries[0].copy(result = 13),
                testObj.tries[0].copy(result = 17)
            ))
            .updateTriesWithChosenOne()

        assertEquals(listOf(true, false), testObj.tries.map { it.isChosen })
    }

    companion object {
        private val finishedThrows = listOf(5, 4, 3, 2)
    }
}