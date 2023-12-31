package com.krystianrymonlipinski.dicepouch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollState
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

@Composable
fun LaunchedRollProcess(
    currentState: RollState,
    rollingSettings: RollingSettings,
    onSingleThrowStarted: () -> Unit,
    onNewRandomValue: (Int) -> Unit,
    onRandomizationEnded: () -> Unit,
    onSingleThrowFinished: () -> Unit,
    onTryFinished: () -> Unit,
    onRollFinished: () -> Unit
) {
    if (!currentState.isFinished) {
        LaunchedEffect(key1 = currentState.setting) { //TODO: fix LaunchedEffect key for changing orientation
            performRoll(
                currentState = currentState,
                rollingSettings = rollingSettings,
                onSingleThrowStarted = onSingleThrowStarted,
                onNewRandomValue = onNewRandomValue,
                onRandomizationEnded = onRandomizationEnded,
                onSingleThrowFinished = onSingleThrowFinished,
                onTryFinished = onTryFinished,
                onRollFinished = onRollFinished
            )
        }
    }
}

private suspend fun performRoll(
    currentState: RollState,
    rollingSettings: RollingSettings,
    onSingleThrowStarted: () -> Unit,
    onNewRandomValue: (Int) -> Unit,
    onRandomizationEnded: () -> Unit,
    onSingleThrowFinished: () -> Unit,
    onTryFinished: () -> Unit,
    onRollFinished: () -> Unit
) {
    val setting = currentState.setting

    delay(INITIAL_ROLL_DELAY)
    for (tryNumber in currentState.currentTry until setting.numberOfTries + 1) {
        performTry(
            currentState = currentState,
            rollingSettings = rollingSettings,
            onSingleThrowStarted = onSingleThrowStarted,
            onNewRandomValue = onNewRandomValue,
            onRandomizationEnded = onRandomizationEnded,
            onSingleThrowFinished = onSingleThrowFinished
        )
        if (setting.modifier != 0 || setting.diceNumber > 1) {
            delay(SHOW_TRY_RESULT_DELAY)
        }
        onTryFinished()
        if (tryNumber < setting.numberOfTries) {
            delay(rollingSettings.delayBetweenThrowsTimeMillis.toLong())
        }
    }

    if (setting.numberOfTries > 1) {
        delay(SHOW_ROLL_RESULT_DELAY)
    }
    onRollFinished()
}

private suspend fun performTry(
    currentState: RollState,
    rollingSettings: RollingSettings,
    onSingleThrowStarted: () -> Unit,
    onNewRandomValue: (Int) -> Unit,
    onRandomizationEnded: () -> Unit,
    onSingleThrowFinished: () -> Unit
) {
    val setting = currentState.setting

    for (throwNumber in currentState.currentThrow until setting.diceNumber + 1) {
        onSingleThrowStarted()
        performThrow(
            die = setting.die,
            throwTimeSettingMillis = rollingSettings.singleThrowTimeMillis,
            onNewRandomValue = onNewRandomValue
        )
        onRandomizationEnded()
        delay(SHOW_THROW_RESULT_DELAY)
        onSingleThrowFinished()
        if (throwNumber < setting.diceNumber) {
            delay(rollingSettings.delayBetweenThrowsTimeMillis.toLong())
        }
    }
}

private suspend fun performThrow(
    die: Die,
    throwTimeSettingMillis: Int,
    onNewRandomValue: (Int) -> Unit,
) {
    /*TODO: save randomizer timer when changing orientation
    For now it will reset to 0, so theoretically you can throw one die eternally if changing it fast enough */
    var elapsedTime = 0L
    while (elapsedTime < throwTimeSettingMillis) {
        elapsedTime += measureTimeMillis {
            onNewRandomValue(die.roll())
            delay(DELAY_BETWEEN_RANDOMS)
        }
    }
}

private const val INITIAL_ROLL_DELAY = 800L
private const val DELAY_BETWEEN_RANDOMS = 10L

private const val SHOW_RESULT_DELAY =  500L
private const val SHOW_THROW_RESULT_DELAY = SHOW_RESULT_DELAY
private const val SHOW_TRY_RESULT_DELAY = SHOW_RESULT_DELAY
private const val SHOW_ROLL_RESULT_DELAY = SHOW_RESULT_DELAY