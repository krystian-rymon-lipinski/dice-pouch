package com.krystianrymonlipinski.dicepouch.ui.dialogs

import RollDescription
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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

@Composable
fun RollDialog(
    stateHolder: RollDialogStateHolder = RollDialogStateHolder(rollSetting = RollSetting(Die(6), 4, 2)),
    onConfirmButtonClicked: () -> Unit = {},
) {

    val rollState by rememberSaveable {
        stateHolder.rollState
    }

    AlertDialog(
        onDismissRequest = { /* Current properties do not allow this */ },
        confirmButton = { ConfirmButton(
            onConfirmButtonClicked = onConfirmButtonClicked
        ) },
        text = { RollDialogContent(
            setting = stateHolder.rollSetting,
            state = rollState
        ) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}

@Composable
fun RollDialogContent(
    setting: RollSetting,
    state: RollState
) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RollDescription(
                description = setting.rollDescription,
                textStyle = MaterialTheme.typography.titleSmall
            )
            CurrentThrow()
            if(setting.diceNumber > 1 || setting.mechanic != RollSetting.Mechanic.NORMAL) {
                RollOutcome(setting)
            }
        }
}

@Composable
fun CurrentThrow() {
    Text(
        text = "11",
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        style = MaterialTheme.typography.displayLarge
    )
}

@Composable
fun RollOutcome(setting: RollSetting) {
    Column(modifier = Modifier.border(
        width = 2.dp,
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.extraSmall
    )) {
        ThrowOutcomes(setting)
        if (setting.mechanic != RollSetting.Mechanic.NORMAL) {
            Spacer(modifier = Modifier.height(4.dp))
            ThrowOutcomes(setting)
        }
    }
}

@Composable
fun ThrowOutcomes(setting: RollSetting) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1.. setting.diceNumber) {
            DieCell(die = setting.die, onDieClicked = {})
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
    val rollState = mutableStateOf(RollState())

    fun changeRollProgress(newState: RollState.Progress) {
        rollState.value = rollState.value.copy(progress = newState)
    }

    fun markNextThrow() {
        rollState.value = rollState.value.copy(throwNumber = rollState.value.throwNumber.inc())
    }

    fun markNextTry() {
        rollState.value = rollState.value.copy(
            tryNumber = rollState.value.tryNumber.inc(),
            throwNumber = 1
        )
    }
}