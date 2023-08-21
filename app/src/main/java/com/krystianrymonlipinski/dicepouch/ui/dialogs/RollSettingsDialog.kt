package com.krystianrymonlipinski.dicepouch.ui.dialogs

import RollDescription
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import com.google.android.material.button.MaterialButtonToggleGroup
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun RollSettingsDialog(
    stateHolder: RollSettingsStateHolder = RollSettingsStateHolder(Die(6)),
    onDismissRequest: () -> Unit = {},
    onRollButtonClicked: (RollSetting) -> Unit = {}
) {
    val savedState = rememberSaveable { stateHolder.state }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            RollButton(onRollButtonClicked = {
                onDismissRequest()
                onRollButtonClicked(savedState.value) }
            )
        },
        text = { RollSettingsDialogContent(
            savedState.value,
            onDiceNumberChanged = { stateHolder.changeDiceNumber(it) },
            onModifierChanged = { stateHolder.changeModifier(it) },
            onMechanicSettingChanged = {
                stateHolder.changeMechanic(it)
                if (it != RollSetting.Mechanic.NORMAL) {
                    stateHolder.resetDiceNumber()
                }
            }
        ) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
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
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            state = state
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            RollSetting(
                settingName = stringResource(id = R.string.roll_setting_dice_number),
                shouldDisableControls = state.mechanic != RollSetting.Mechanic.NORMAL,
                onIncrementClicked = { onDiceNumberChanged(1) },
                onDecrementClicked = { onDiceNumberChanged(-1) }
            )
            RollSetting(
                settingName = stringResource(id = R.string.roll_setting_modifier),
                shouldDisableControls = false,
                onIncrementClicked = { onModifierChanged(1) },
                onDecrementClicked = { onModifierChanged(-1) }
            )
        }
        MechanicSetting(onMechanicSettingChanged)
    }
}

@Composable
fun RollSetting(
    settingName: String,
    shouldDisableControls: Boolean,
    onIncrementClicked: () -> Unit,
    onDecrementClicked: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onDecrementClicked() }, enabled = !shouldDisableControls) {
            Icon(imageVector = Icons.Filled.Remove, contentDescription = "minus")
        }
        Text(text = settingName)
        IconButton(onClick = { onIncrementClicked() }, enabled = !shouldDisableControls) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "plus")
        }
    }
}

@Composable
fun MechanicSetting(onMechanicSettingChanged: (RollSetting.Mechanic) -> Unit) {
    AndroidView(
        factory = { View.inflate(it, R.layout.advantages_button, null) },
        update = {
            it.findViewById<MaterialButtonToggleGroup>(R.id.adv_setting_button).apply {
                addOnButtonCheckedListener { group, _, _ ->
                    val newSetting = when (group.checkedButtonId) {
                        R.id.button_adv -> RollSetting.Mechanic.ADVANTAGE
                        R.id.button_disadv -> RollSetting.Mechanic.DISADVANTAGE
                        R.id.button_normal -> RollSetting.Mechanic.NORMAL
                        else -> RollSetting.Mechanic.NORMAL
                    }
                    onMechanicSettingChanged(newSetting)
                }
            }
        }
    )
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


class RollSettingsStateHolder(die: Die) {

    val state = mutableStateOf(RollSetting(die))

    fun changeDiceNumber(change: Int) {
        state.value = state.value.copy(diceNumber = state.value.diceNumber + change)
        if (state.value.diceNumber < MIN_DICE) {
            state.value = state.value.copy(diceNumber = MAX_DICE)
        } else if (state.value.diceNumber > MAX_DICE) {
            state.value = state.value.copy(diceNumber = MIN_DICE)
        }
    }

    fun changeModifier(change: Int) {
        state.value = state.value.copy(modifier = state.value.modifier + change)
        if (state.value.modifier < MIN_MOD) {
            state.value = state.value.copy(modifier = MAX_MOD)
        } else if (state.value.modifier > MAX_MOD) {
            state.value = state.value.copy(modifier = MIN_MOD)
        }
    }

    fun changeMechanic(newValue: RollSetting.Mechanic) {
        state.value = state.value.copy(mechanic = newValue)
    }

    fun resetDiceNumber() {
        state.value = state.value.copy(diceNumber = 1)
    }
}


private const val MIN_DICE = 1
private const val MAX_DICE = 30
private const val MIN_MOD = -30
private const val MAX_MOD = 30