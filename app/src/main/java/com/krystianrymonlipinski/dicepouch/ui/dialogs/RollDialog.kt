package com.krystianrymonlipinski.dicepouch.ui.dialogs

import RollDescription
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.krystianrymonlipinski.dicepouch.LaunchedRollProcess
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollState
import com.krystianrymonlipinski.dicepouch.model.TryState
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.components.CenteredDialogConfirmButton
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme


@Composable
fun RollDialog(
    setting: RollSetting = RollSetting(Die(6), 2, 2),
    onConfirmButtonClicked: () -> Unit = {},
) {
    val dialogStateHolder = rememberRollDialogStateHolder(setting)

    LaunchedRollProcess(
        currentState = dialogStateHolder.rollState,
        onNewRandomValue = { newValue -> dialogStateHolder.updateRandomizer(newValue) },
        onSingleThrowFinished = {
            dialogStateHolder.addThrowResult()
            dialogStateHolder.clearRandomizer()
            dialogStateHolder.markNextThrow()
        },
        onTryFinished = {
            dialogStateHolder.calculateTryResult()
            dialogStateHolder.markNextTry()
        },
        onRollFinished = {
            dialogStateHolder.markChosenTry()
        }
    )

    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        onDismissRequest = { /* Current properties do not allow this */ },
        confirmButton = {
            val rollResult = dialogStateHolder.rollState.tries.find { it.isChosen }?.result
            CenteredDialogConfirmButton(
                text = rollResult?.let {
                    stringResource(id = R.string.btn_roll_confirm_with_result, it)
                } ?: stringResource(id = R.string.btn_roll_cancel),
                onClick = onConfirmButtonClicked
            )
        },
        text = { RollDialogContent(
            rollState = dialogStateHolder.rollState,
            randomizerState = dialogStateHolder.randomizerState,
        ) }
    )
}

@Composable
fun rememberRollDialogStateHolder(setting: RollSetting) : RollDialogStateHolder {
    return rememberSaveable(saver = RollDialogStateHolder.Saver) {
        RollDialogStateHolder(rollState = RollState(setting))
    }
}

@Composable
fun RollDialogContent(
    rollState: RollState,
    randomizerState: Int?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RollDescription(
            description = rollState.setting.rollDescription,
            textStyle = MaterialTheme.typography.titleMedium
        )
        CurrentThrow(randomizerState = randomizerState)
        RollResult(rollState = rollState)

    }
}

@Composable
fun CurrentThrow(
    modifier: Modifier = Modifier,
    randomizerState: Int?
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .padding(vertical = 24.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = randomizerState?.toString() ?: "",
            modifier = Modifier.padding(vertical = 4.dp),
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
            TryCalculation(
                setting = rollState.setting,
                tryState = rollState.tries[itemNumber],
                isCurrentTry = rollState.currentTry - 1 == itemNumber,
                currentThrow = rollState.currentThrow
            )
        }
    }
}

@Composable
fun TryCalculation(setting: RollSetting, tryState: TryState, isCurrentTry: Boolean, currentThrow: Int) {
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TryResult(result = tryState.result, isChosen = tryState.isChosen)
        Text(text = " = ", style = MaterialTheme.typography.headlineSmall)
        DiceSum(setting = setting, throws = tryState.throws, isCurrentTry = isCurrentTry, currentThrow = currentThrow)
    }
}

@Composable
fun TryResult(result: Int?, isChosen: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.15f)
            .height(48.dp)
            .conditionalBorder(isChosen) {
                this.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraSmall
                )
            },
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.secondaryContainer

    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = result?.toString() ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun DiceSum(setting: RollSetting, throws: List<Int?>, isCurrentTry: Boolean, currentThrow: Int) {
    LazyRow(
        modifier = Modifier.semantics { contentDescription = "dice_sum_scrollable_row" },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (setting.modifier != 0) {
            item { Text(text = setting.modifier.toString(), style = MaterialTheme.typography.headlineSmall) }
        }
        items(throws.size) { throwNumber ->
            if (setting.modifier != 0 || throwNumber != 0) {
                Text(
                    text = "+",
                    modifier = Modifier.padding(horizontal = 2.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            DieImage(
                die = setting.die,
                valueShown = throws[throwNumber]?.toString() ?: "",
                modifier = Modifier
                    .size(48.dp)
                    .conditionalBorder(isCurrentTry && currentThrow - 1 == throwNumber) {
                        //TODO: animate border hopping from die to die
                        this.border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                    },
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun Modifier.conditionalBorder(condition: Boolean, modifier: @Composable Modifier.() -> Modifier) =
    then(if (condition) modifier.invoke(this) else this)


@Composable
@Preview
fun RollDialogPreview() {
    DicePouchTheme {
        RollDialog()
    }
}

class RollDialogStateHolder(
    rollState: RollState,
    randomizerState: Int? = null
) {
    var rollState by mutableStateOf(rollState)
    var randomizerState by mutableStateOf(randomizerState)

    fun addThrowResult() {
        rollState = rollState.updateTryWithNewThrow(randomizerState ?: 0)
    }

    fun markNextThrow() {
        rollState = rollState.markNextThrow()
    }

    fun calculateTryResult() {
        rollState = rollState.updateTryWithResult()
    }

    fun markNextTry() {
        rollState = rollState.markNextTry()
    }

    fun markChosenTry() {
        rollState = rollState.updateTriesWithChosenOne()
    }

    fun updateRandomizer(newValue: Int) { randomizerState = newValue }
    fun clearRandomizer() { randomizerState = null }

    companion object {
        val Saver: Saver<RollDialogStateHolder, *> = listSaver(
            save = { listOf(it.rollState, it.randomizerState) },
            restore = { RollDialogStateHolder(
                rollState = it[0] as RollState,
                randomizerState = it[1] as Int?
            ) }
        )
    }
}
