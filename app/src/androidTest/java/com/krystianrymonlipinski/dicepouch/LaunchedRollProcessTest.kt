package com.krystianrymonlipinski.dicepouch

import androidx.compose.runtime.Composable
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollState
import org.junit.Assert.assertEquals
import org.junit.Test

class LaunchedRollProcessTest : BaseAndroidTest() {


    @Test
    fun rollProcess_checkNumberOfThrows() {
        var rollState = RollState(setting = RollSetting(die = Die(6), diceNumber = 5, mechanic = RollSetting.Mechanic.ADVANTAGE))
        var rollingFinished = false

        composeTestRule.setContent {
            LaunchedTestRollProcess(
                state = rollState,
                onSingleThrowFinished = { rollState = rollState.markNextThrow() },
                onRollFinished = { rollingFinished = true }
            )
        }
        composeTestRule.waitUntil(timeoutMillis = 60_000) { rollingFinished }
        assertEquals(11, rollState.currentThrow)
    }

    @Test
    fun rollProcess_withOrientationChange_duringRoll() {
        var rollState = RollState(setting = RollSetting(die = Die(6), diceNumber = 4))
        var rollingFinished = false

        restorationTester.setContent {
            LaunchedTestRollProcess(
                state = rollState,
                onSingleThrowFinished = { rollState = rollState.markNextThrow() },
                onRollFinished = { rollingFinished = true }
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 60_000) { rollState.currentThrow == 3 }
        restorationTester.emulateSavedInstanceStateRestore()
        assertEquals(3, rollState.currentThrow)
        composeTestRule.waitUntil(timeoutMillis = 60_000) { rollingFinished }
        assertEquals(5, rollState.currentThrow)
    }

    @Test
    fun rollProcess_withOrientationChange_afterRoll() {
        var rollState = RollState(setting = RollSetting(die = Die(6), diceNumber = 3))
        var rollingFinished = false

        restorationTester.setContent {
            LaunchedTestRollProcess(
                state = rollState,
                onSingleThrowFinished = { rollState = rollState.markNextThrow() },
                onRollFinished = { rollingFinished = true }
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 60_000) { rollingFinished }
        assertEquals(4, rollState.currentThrow)
        restorationTester.emulateSavedInstanceStateRestore()
        assertEquals(4, rollState.currentThrow)
        composeTestRule.mainClock.advanceTimeBy(milliseconds = 60_000) // check if LaunchedEffect has restarted; it shouldn't
        assertEquals(4, rollState.currentThrow)
    }

    @Composable
    fun LaunchedTestRollProcess(
        state: RollState,
        onSingleThrowFinished: () -> Unit,
        onRollFinished: () -> Unit
    ) {
        LaunchedRollProcess(
            currentState = state,
            onNewRandomValue = { },
            onSingleThrowFinished = onSingleThrowFinished,
            onTryFinished = { },
            onRollFinished = onRollFinished
        )
    }

}