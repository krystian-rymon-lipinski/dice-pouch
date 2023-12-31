package com.krystianrymonlipinski.dicepouch.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.ui.components.CenteredDialogConfirmButton
import com.krystianrymonlipinski.dicepouch.ui.components.MechanicSegmentedButton
import com.krystianrymonlipinski.dicepouch.ui.components.RollDescription
import com.krystianrymonlipinski.dicepouch.ui.components.RollSettingRow
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun RollSettingsDialog(
    die: Die = Die(6),
    onDismissDialog: () -> Unit = {},
    onRollButtonClicked: (RollSetting) -> Unit = {}
) {
    val dialogStateHolder = rememberSettingsDialogStateHolder(die = die)

    AlertDialog(
        onDismissRequest = { onDismissDialog() },
        confirmButton = {
            CenteredDialogConfirmButton(
                text = stringResource(id = R.string.btn_roll),
                onClick = { onRollButtonClicked(dialogStateHolder.state) })
        },
        text = { RollSettingsDialogContent(
            dialogStateHolder.state,
            onDiceNumberChanged = { dialogStateHolder.changeDiceNumber(it) },
            onModifierChanged = { dialogStateHolder.changeModifier(it) },
            onMechanicSettingChanged = { dialogStateHolder.changeMechanic(it) }
        ) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun rememberSettingsDialogStateHolder(die: Die) : RollSettingsDialogStateHolder {
    return rememberSaveable(saver = RollSettingsDialogStateHolder.Saver) {
        RollSettingsDialogStateHolder(rollSetting = RollSetting(die))
    }
}

@Composable
fun RollSettingsDialogContent(
    state: RollSetting,
    onDiceNumberChanged: (Int) -> Unit,
    onModifierChanged: (Int) -> Unit,
    onMechanicSettingChanged: (RollSetting.Mechanic) -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RollDescription(
            description = state.rollDescription,
            modifier = Modifier.padding(bottom = 24.dp),
            textStyle = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        RollSettingRow(
            settingName = stringResource(id = R.string.roll_setting_dice_number),
            onIncrementClicked = { onDiceNumberChanged(1) },
            onDecrementClicked = { onDiceNumberChanged(-1) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        RollSettingRow(
            settingName = stringResource(id = R.string.roll_setting_modifier),
            onIncrementClicked = { onModifierChanged(1) },
            onDecrementClicked = { onModifierChanged(-1) }
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
        )
        MechanicSegmentedButton(
            die = state.die,
            onSelectedButtonChanged = { newMechanic -> onMechanicSettingChanged(newMechanic) }
        )
    }
}


@Composable
@Preview
fun RollSettingsDialogPreview() {
    DicePouchTheme {
        RollSettingsDialog()
    }
}


class RollSettingsDialogStateHolder(rollSetting: RollSetting) {

    var state by mutableStateOf(rollSetting)

    fun changeDiceNumber(change: Int) {
        state = state.changeDiceNumber(change = change)
    }

    fun changeModifier(change: Int) {
        state = state.changeModifier(change = change)
    }

    fun changeMechanic(newValue: RollSetting.Mechanic) {
        state = state.changeMechanic(newValue = newValue)
    }

    companion object {
        val Saver: Saver<RollSettingsDialogStateHolder, *> = listSaver(
            save = { listOf(it.state) },
            restore = { RollSettingsDialogStateHolder(rollSetting = it[0]) }
        )
    }
}