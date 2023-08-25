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
import com.krystianrymonlipinski.dicepouch.ui.components.DieCell
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

@Composable
fun RollDialog(
    stateHolder: RollDialogStateHolder = RollDialogStateHolder(rollSetting = RollSetting(Die(6), 2, 2)),
    onConfirmButtonClicked: () -> Unit = {},
) {

    val rollState = rememberSaveable {
        stateHolder.rollState
    }
    val randomizerState by remember {
        stateHolder.randomizerState
    }

    LaunchedRollProcess(
        setting = stateHolder.rollSetting,
        onNewRandomValue = { newValue -> stateHolder.updateRandomizer(newValue) },
        onSingleThrowFinished = {
            stateHolder.addThrowResult()
            stateHolder.markNextThrow()
            stateHolder.clearRandomizer()
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
    randomizerState: Int?
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
        RollResult(
            setting = setting,
            rollState = rollState
        )
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
fun RollResult(
    setting: RollSetting,
    rollState: RollState
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(setting.numberOfTries) { tryNumber ->
            TryResult(
                setting = setting,
                outcomes = rollState.throwResults[tryNumber],
                tryNumber = rollState.currentTry
            )
        }
    }
}

@Composable
fun TryResult(
    setting: RollSetting,
    outcomes: List<Int?>,
    tryNumber: Int
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (setting.numberOfTries > 1) {
            Text(text = "$tryNumber: ", style = MaterialTheme.typography.headlineSmall)
        }
        Text(
            text = "",
            modifier = Modifier
                .width(40.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.extraSmall),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = " = ")
        setting.generateModifierText()?.let { Text(text = it) }

        for (i in 1.. setting.diceNumber) {
            if (setting.modifier != 0 || i != 1) {
                Text(text = "+", modifier = Modifier.padding(horizontal = 2.dp))
            }
            DieCell(
                die = setting.die,
                valueShown = outcomes[i-1]?.toString() ?: "",
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
            delay(RANDOMIZER_FREEZE)
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
    var randomizerState = mutableStateOf<Int?>(null)

    init {
        for (i in 0 until rollSetting.diceNumber) {
            rollState.throwResults[0].add(null)
            rollState.throwResults[1].add(null)
        }
    }

    fun addThrowResult() {
        rollState.throwResults[rollState.currentTry - 1][rollState.currentThrow - 1] = randomizerState.value ?: 0
    }

    fun markNextThrow() {
        rollState = rollState.copy(currentThrow = rollState.currentThrow.inc())
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
private const val RANDOMIZER_FREEZE = 500L
private const val DELAY_BETWEEN_THROWS = 500L
private const val RANDOMIZING_TIME = 1000L
private const val DELAY_BETWEEN_RANDOMS = 10L
