package com.krystianrymonlipinski.dicepouch.ui.dialogs

import RollDescription
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.ui.components.CenteredDialogConfirmButton
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.components.icons.LeftArrow
import com.krystianrymonlipinski.dicepouch.ui.components.MechanicSegmentedButton
import com.krystianrymonlipinski.dicepouch.ui.components.icons.RightArrow
import com.krystianrymonlipinski.dicepouch.ui.components.RollSettingRow
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun RollShortcutDialog(
    shortcut: RollShortcut? = null,
    diceInSet: List<Die> = emptyList(),
    onDialogDismissed: () -> Unit = { },
    onSaveShortcutClicked: (RollShortcut) -> Unit = { }
) {
    val newShortcutString = stringResource(id = R.string.new_shortcut)
    var shortcutState by rememberSaveable {
        mutableStateOf(shortcut ?: run {
                RollShortcut(
                    timestampId = 0L, /* Correct timestamp will be assigned in view model after confirming shortcut setting */
                    name = newShortcutString,
                    setting = RollSetting(diceInSet[0])
                )
        })
    }

    AlertDialog(
        onDismissRequest = onDialogDismissed,
        confirmButton = { CenteredDialogConfirmButton(
            text = stringResource(id = R.string.btn_dialog_save),
            onClick = { onSaveShortcutClicked(shortcutState) },
            isEnabled = shortcutState.name.isNotBlank()
        )},
        text = { RollShortcutDialogContent(
            shortcutState = shortcutState,
            diceInSet = diceInSet,
            onNameChanged = { newName -> shortcutState = shortcutState.changeName(newName) },
            onSettingChanged = { newSetting -> shortcutState = shortcutState.changeSetting(newSetting) }
        ) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun RollShortcutDialogContent(
    shortcutState: RollShortcut,
    diceInSet: List<Die>,
    onNameChanged: (String) -> Unit,
    onSettingChanged: (RollSetting) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        RollDescription(
            description = shortcutState.setting.rollDescription,
            textStyle = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        NameInputField(
            currentName = shortcutState.name,
            onNameChanged = onNameChanged
        )
        Spacer(modifier = Modifier.height(16.dp))
        ShortcutSettingColumn(
            rollSetting = shortcutState.setting,
            diceInSet = diceInSet,
            onSettingChanged = onSettingChanged
        )
    }
}

@Composable
fun NameInputField(
    currentName: String,
    onNameChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = currentName,
        onValueChange = onNameChanged
    )
}

@Composable
fun ShortcutSettingColumn(
    rollSetting: RollSetting,
    diceInSet: List<Die>,
    onSettingChanged: (RollSetting) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ChoosingDieRow(
            chosenDie = rollSetting.die,
            diceInSet = diceInSet,
            onDieChanged = { onSettingChanged(rollSetting.changeDie(it)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        RollSettingRow(
            settingName = stringResource(id = R.string.roll_setting_dice_number),
            onIncrementClicked = { onSettingChanged(rollSetting.changeDiceNumber(change = 1)) },
            onDecrementClicked = { onSettingChanged(rollSetting.changeDiceNumber(change = -1)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        RollSettingRow(
            settingName = stringResource(id = R.string.roll_setting_modifier),
            onIncrementClicked = { onSettingChanged(rollSetting.changeModifier(change = 1)) },
            onDecrementClicked = { onSettingChanged(rollSetting.changeModifier(change = -1)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MechanicSegmentedButton(
            die = rollSetting.die,
            selectedState = rollSetting.mechanic,
            onSelectedButtonChanged = { newValue -> onSettingChanged(rollSetting.changeMechanic(newValue = newValue)) }
        )
    }
}

@Composable
fun ChoosingDieRow(
    chosenDie: Die,
    diceInSet: List<Die>,
    onDieChanged: (Die) -> Unit
) {
    val indexOfChosenDie = diceInSet.indexOf(chosenDie)
    Row(verticalAlignment = Alignment.CenterVertically) {
        LeftArrow(
            onIconClicked = { onDieChanged(
                if (indexOfChosenDie == 0) diceInSet.last() else diceInSet[indexOfChosenDie - 1]
            )},
            isEnabled = diceInSet.size > 1
        )
        Spacer(modifier = Modifier.width(16.dp))
        DieImage(die = chosenDie)
        Spacer(modifier = Modifier.width(16.dp))
        RightArrow(
            onIconClicked = { onDieChanged(
                if(indexOfChosenDie == diceInSet.size - 1) diceInSet.first() else diceInSet[indexOfChosenDie + 1]
            )},
            isEnabled = diceInSet.size > 1
        )
    }
}


@Composable
@Preview
fun RollShortcutDialogPreview() {
    DicePouchTheme {
        RollShortcutDialog()
    }
}