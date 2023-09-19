package com.krystianrymonlipinski.dicepouch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollState
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.system.measureTimeMillis

@Composable
fun LaunchedRollProcess(
    currentState: RollState,
    onNewRandomValue: (Int) -> Unit,
    onSingleThrowFinished: () -> Unit,
    onTryFinished: () -> Unit,
    onRollFinished: () -> Unit
) {
    if (!currentState.isFinished) {
        LaunchedEffect(key1 = currentState.setting) { //TODO: fix LaunchedEffect key for changing orientation
            performRoll(
                currentState = currentState,
                onNewRandomValue = onNewRandomValue,
                onSingleThrowFinished = onSingleThrowFinished,
                onTryFinished = onTryFinished,
                onRollFinished = onRollFinished
            )
        }
    }
}

private suspend fun performRoll(
    currentState: RollState,
    onNewRandomValue: (Int) -> Unit,
    onSingleThrowFinished: () -> Unit,
    onTryFinished: () -> Unit,
    onRollFinished: () -> Unit
) {
    val setting = currentState.setting

    delay(INITIAL_ROLL_DELAY)
    for (tryNumber in currentState.currentTry until setting.numberOfTries + 1) {
        performTry(
            currentState = currentState,
            onNewRandomValue = onNewRandomValue,
            onSingleThrowFinished = onSingleThrowFinished
        )
        if (setting.modifier != 0 || setting.diceNumber > 1) {
            delay(SHOW_TRY_RESULT_DELAY)
        }
        onTryFinished()
        if (tryNumber < setting.numberOfTries) {
            delay(DELAY_BETWEEN_THROWS)
        }
    }

    if (setting.numberOfTries > 1) {
        delay(SHOW_ROLL_RESULT_DELAY)
    }
    onRollFinished()
}

private suspend fun performTry(
    currentState: RollState,
    onNewRandomValue: (Int) -> Unit,
    onSingleThrowFinished: () -> Unit
) {
    val setting = currentState.setting

    for (throwNumber in currentState.currentThrow until setting.diceNumber + 1) {
        Timber.d("HERE; throw number = $throwNumber")
        performThrow(
            die = setting.die,
            onNewRandomValue = onNewRandomValue
        )
        delay(SHOW_THROW_RESULT_DELAY)
        onSingleThrowFinished()
        if (throwNumber < setting.diceNumber) {
            delay(DELAY_BETWEEN_THROWS)
        }
    }
}

private suspend fun performThrow(
    die: Die,
    onNewRandomValue: (Int) -> Unit,
) {
    /*TODO: save randomizer timer when changing orientation
    For now it will reset to 0, so theoretically you can throw one die eternally if changing it fast enough */
    var elapsedTime = 0L
    while (elapsedTime < RANDOMIZING_TIME) {
        //Timber.d("HERE; throw elapsed time = $elapsedTime")
        elapsedTime += measureTimeMillis {
            onNewRandomValue(die.roll())
            delay(DELAY_BETWEEN_RANDOMS)
        }
    }
}

private const val INITIAL_ROLL_DELAY = 800L
private const val RANDOMIZING_TIME = 1000L
private const val DELAY_BETWEEN_RANDOMS = 10L
private const val DELAY_BETWEEN_THROWS = 500L

private const val SHOW_RESULT_DELAY =  500L
private const val SHOW_THROW_RESULT_DELAY = SHOW_RESULT_DELAY
private const val SHOW_TRY_RESULT_DELAY = SHOW_RESULT_DELAY
private const val SHOW_ROLL_RESULT_DELAY = SHOW_RESULT_DELAY