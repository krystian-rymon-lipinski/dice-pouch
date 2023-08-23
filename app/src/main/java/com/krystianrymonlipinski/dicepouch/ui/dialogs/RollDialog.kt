package com.krystianrymonlipinski.dicepouch.ui.dialogs

import RollDescription
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollState
import com.krystianrymonlipinski.dicepouch.ui.components.DieCell
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

@Composable
fun RollDialog(
    stateHolder: RollDialogStateHolder = RollDialogStateHolder(rollSetting = RollSetting(Die(6), 4, 2)),
    onConfirmButtonClicked: () -> Unit = {},
) {

    val rollState = rememberSaveable {
        stateHolder.rollState
    }
    var randomizerState by remember {
        stateHolder.randomizerState
    }

    LaunchedRollProcess(
        setting = stateHolder.rollSetting,
        onNewRandomValue = { newValue -> randomizerState = newValue },
        onSingleThrowFinished = {
            stateHolder.addThrowResult(randomizerState)
            stateHolder.markNextThrow()
            randomizerState = 0
        },
        onTryFinished = { stateHolder.markNextTry() },
        onRollingFinished = {  }
    )

    AlertDialog(
        onDismissRequest = { /* Current properties do not allow this */ },
        confirmButton = { ConfirmButton(
            onConfirmButtonClicked = onConfirmButtonClicked
        ) },
        text = { RollDialogContent(
            setting = stateHolder.rollSetting,
            rollState = rollState,
            randomizerState = randomizerState
        ) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}

@Composable
fun RollDialogContent(
    setting: RollSetting,
    rollState: RollState,
    randomizerState: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RollDescription(
            description = setting.rollDescription,
            textStyle = MaterialTheme.typography.titleSmall
        )
        CurrentThrow(randomizerState)
        RollOutcome(
            setting = setting,
            outcomes = rollState.outcomes
        )
    }
}

@Composable
fun CurrentThrow(randomizerState: Int) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = if (randomizerState != 0) randomizerState.toString() else "",
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            style = MaterialTheme.typography.displayLarge
        )
    }

}

@Composable
fun RollOutcome(
    setting: RollSetting,
    outcomes: List<MutableList<Int>>
) {
    LazyColumn(
        modifier = Modifier.border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.extraSmall
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(setting.numberOfTries) { tryNumber ->
            ThrowOutcomes(setting = setting, outcomes = outcomes[tryNumber])
        }
    }
}

@Composable
fun ThrowOutcomes(
    setting: RollSetting,
    outcomes: List<Int>
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1.. setting.diceNumber) {
            DieCell(
                die = setting.die,
                onDieClicked = {},
                valueShown = outcomes[i-1].toString()
            )
            if (i < setting.diceNumber) {
                Text(text = "+", modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
        setting.generateModifierText()?.let { Text(text = it) }
    }
}

@Composable
fun ConfirmButton(onConfirmButtonClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(onClick = { onConfirmButtonClicked() }) {
            Text(
                text = stringResource(id = R.string.btn_ok),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

/* --------- */

@Composable
fun LaunchedRollProcess(
    setting: RollSetting,
    onNewRandomValue: (Int) -> Unit,
    onSingleThrowFinished: () -> Unit,
    onTryFinished: () -> Unit,
    onRollingFinished: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        delay(INITIAL_ROLL_DELAY)
        launch { repeat(setting.numberOfTries) {
                val tryJob = getSingleTryJob(
                    scope = this,
                    setting = setting,
                    onNewRandomValue = onNewRandomValue,
                    onSingleThrowFinished = onSingleThrowFinished
                )
                tryJob.join()
            onTryFinished()
        } }
        onRollingFinished()
    }
}

private suspend fun getSingleTryJob(
    scope: CoroutineScope,
    setting: RollSetting,
    onNewRandomValue: (Int) -> Unit,
    onSingleThrowFinished: () -> Unit
) : Job {
    return scope.launch { repeat(setting.diceNumber) {
            val throwJob = getSingleDieThrowJob(
                die = setting.die,
                scope = scope,
                onNewRandomValue = onNewRandomValue
            )
            throwJob.join() /* Wait for the random to end before starting next throw */
            onSingleThrowFinished()
            delay(DELAY_BETWEEN_THROWS)
    } }
}

private suspend fun getSingleDieThrowJob(
    die: Die,
    scope: CoroutineScope,
    onNewRandomValue: (Int) -> Unit,
) : Job {
    return scope.launch {
        var elapsedTime = 0L
        while (elapsedTime < RANDOMIZING_TIME) {
            elapsedTime += measureTimeMillis {
                onNewRandomValue(die.roll())
                delay(DELAY_BETWEEN_RANDOMS)
            }
        }
    }
}


@Composable
@Preview
fun RollDialogPreview() {
    DicePouchTheme {
        RollDialog()
    }
}

class RollDialogStateHolder(
    val rollSetting: RollSetting,
) {
    var rollState by mutableStateOf(RollState())
    val randomizerState = mutableStateOf(0)

    init {
        for (i in 0 until rollSetting.diceNumber) {
            rollState.outcomes[0].add(0)
            rollState.outcomes[1].add(0)
        }
    }

    fun addThrowResult(result: Int) {
        rollState.outcomes[rollState.tryNumber - 1].add(rollState.throwNumber - 1, result)
    }

    fun changeRollProgress(newState: RollState.Progress) {
        rollState = rollState.copy(progress = newState)
    }

    fun markNextThrow() {
        rollState = rollState.copy(throwNumber = rollState.throwNumber.inc())
    }

    fun markNextTry() {
        rollState = rollState.copy(
            tryNumber = rollState.tryNumber.inc(),
            throwNumber = 1
        )
    }
}

private const val INITIAL_ROLL_DELAY = 800L
private const val DELAY_BETWEEN_THROWS = 500L
private const val RANDOMIZING_TIME = 1000L
private const val DELAY_BETWEEN_RANDOMS = 10L
