package com.krystianrymonlipinski.dicepouch.ui.dialogs

import RollDescription
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollState
import com.krystianrymonlipinski.dicepouch.model.TryState
import com.krystianrymonlipinski.dicepouch.ui.components.DieCell
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

@Composable
fun RollDialog(
    stateHolder: RollDialogStateHolder = RollDialogStateHolder(setting = RollSetting(Die(6), 2, 2)),
    onConfirmButtonClicked: () -> Unit = {},
) {

    val rollState = rememberSaveable {
        stateHolder.rollState
    }
    val randomizerState by remember {
        stateHolder.randomizerState
    }

    LaunchedRollProcess(
        setting = stateHolder.rollState.setting,
        onNewRandomValue = { newValue -> stateHolder.updateRandomizer(newValue) },
        onSingleThrowFinished = {
            stateHolder.addThrowResult()
            stateHolder.clearRandomizer()
            stateHolder.markNextThrow()
        },
        onTryFinished = {
            stateHolder.calculateTryResult()
            stateHolder.markNextTry()
        },
        onRollingFinished = {  }
    )

    AlertDialog(
        onDismissRequest = { /* Current properties do not allow this */ },
        confirmButton = { ConfirmButton(
            onConfirmButtonClicked = onConfirmButtonClicked
        ) },
        text = { RollDialogContent(
            rollState = rollState,
            randomizerState = randomizerState
        ) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}

@Composable
fun RollDialogContent(
    rollState: RollState,
    randomizerState: Int?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RollDescription(
            description = rollState.setting.rollDescription,
            textStyle = MaterialTheme.typography.titleSmall
        )
        CurrentThrow(randomizerState)
        RollResult(rollState = rollState)
    }
}

@Composable
fun CurrentThrow(randomizerState: Int?) {
    Surface(
        modifier = Modifier
            .width(120.dp)
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = randomizerState?.toString() ?: "",
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge
        )
    }

}

@Composable
fun RollResult(rollState: RollState) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(rollState.setting.numberOfTries) { itemNumber ->
            TryResult(
                setting = rollState.setting,
                rowNumber = itemNumber,
                tryState = rollState.tries[itemNumber]
            )
        }
    }
}

@Composable
fun TryResult(
    setting: RollSetting,
    rowNumber: Int,
    tryState: TryState,
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (setting.numberOfTries > 1) {
            Text(text = "${rowNumber + 1}: ", style = MaterialTheme.typography.headlineSmall)
        }
        Text(
            text = tryState.result?.toString() ?: "",
            modifier = Modifier
                .width(40.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.extraSmall),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = " = ")
        setting.generateModifierText()?.let { Text(text = it) }

        for (i in 0 until setting.diceNumber) {
            if (setting.modifier != 0 || i != 0) {
                Text(text = "+", modifier = Modifier.padding(horizontal = 2.dp))
            }
            DieCell(
                die = setting.die,
                valueShown = tryState.throws[i]?.toString() ?: "",
                modifier = Modifier
                    .width(40.dp)
            )
        }
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
                delay(DELAY_BEFORE_SHOWING_RESULT)
                onTryFinished()
                delay(DELAY_BETWEEN_THROWS)
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
            delay(DELAY_BEFORE_SHOWING_RESULT)
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
    setting: RollSetting,
) {
    var rollState by mutableStateOf(RollState(setting))
    var randomizerState = mutableStateOf<Int?>(null)

    fun addThrowResult() {
        rollState.addThrow(randomizerState.value ?: 0)
    }

    fun markNextThrow() {
        rollState = rollState.copy(currentThrow = rollState.currentThrow.inc())
    }

    fun calculateTryResult() {
        rollState.calculateTryResult()
    }

    fun markNextTry() {
        rollState = rollState.copy(
            currentTry = rollState.currentTry.inc(),
            currentThrow = 1
        )
    }

    fun updateRandomizer(newValue: Int) {
        randomizerState.value = newValue
    }

    fun clearRandomizer() {
        randomizerState.value = null
    }
}

private const val INITIAL_ROLL_DELAY = 800L
private const val DELAY_BEFORE_SHOWING_RESULT = 500L
private const val DELAY_BETWEEN_THROWS = 500L
private const val RANDOMIZING_TIME = 1000L
private const val DELAY_BETWEEN_RANDOMS = 10L
