package com.krystianrymonlipinski.dicepouch.ui.dialogs

import RollDescription
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.ui.components.MechanicSegmentedButton
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun RollSettingsDialog(
    die: Die = Die(6),
    onDismissRequest: () -> Unit = {},
    onRollButtonClicked: (RollSetting) -> Unit = {}
) {
    val dialogStateHolder = rememberSettingsDialogStateHolder(die = die)

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            RollButton(onRollButtonClicked = {
                onDismissRequest()
                onRollButtonClicked(dialogStateHolder.state) }
            )
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
        RollSetting(
            settingName = stringResource(id = R.string.roll_setting_dice_number),
            onIncrementClicked = { onDiceNumberChanged(1) },
            onDecrementClicked = { onDiceNumberChanged(-1) }
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
        )
        RollSetting(
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
fun RollSetting(
    settingName: String,
    onIncrementClicked: () -> Unit,
    onDecrementClicked: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ControlIcon(
            imageVector = Icons.Filled.Remove,
            contentDescription = "minus",
            onIconClicked = { onDecrementClicked() }
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = settingName
        )
        ControlIcon(
            imageVector = Icons.Filled.Add,
            contentDescription = "plus",
            onIconClicked = { onIncrementClicked() }
        )
    }
}

@Composable
fun ControlIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onIconClicked: () -> Unit,
) {
    IconButton(
        onClick = { onIconClicked() },
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small)
    ) { Icon(imageVector = imageVector, contentDescription = contentDescription) }
}

@Composable
fun RollButton(onRollButtonClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(onClick = { onRollButtonClicked() }) {
            Text(
                text = stringResource(id = R.string.btn_roll),
                style = MaterialTheme.typography.headlineSmall
            )
        }
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
        state = state.copy(diceNumber = state.diceNumber + change)
        if (state.diceNumber < MIN_DICE) {
            state = state.copy(diceNumber = MAX_DICE)
        } else if (state.diceNumber > MAX_DICE) {
            state = state.copy(diceNumber = MIN_DICE)
        }
    }

    fun changeModifier(change: Int) {
        state = state.copy(modifier = state.modifier + change)
        if (state.modifier < MIN_MOD) {
            state = state.copy(modifier = MAX_MOD)
        } else if (state.modifier > MAX_MOD) {
            state = state.copy(modifier = MIN_MOD)
        }
    }

    fun changeMechanic(newValue: RollSetting.Mechanic) {
        state = state.copy(mechanic = newValue).copy()
    }

    companion object {
        val Saver: Saver<RollSettingsDialogStateHolder, *> = listSaver(
            save = { listOf(it.state) },
            restore = { RollSettingsDialogStateHolder(rollSetting = it[0]) }
        )
    }
}


private const val MIN_DICE = 1
private const val MAX_DICE = 30
private const val MIN_MOD = -30
private const val MAX_MOD = 30