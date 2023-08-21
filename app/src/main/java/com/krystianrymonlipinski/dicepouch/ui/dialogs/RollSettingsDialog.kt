package com.krystianrymonlipinski.dicepouch.ui.dialogs

import android.os.Parcelable
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import com.google.android.material.button.MaterialButtonToggleGroup
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import kotlinx.parcelize.Parcelize

@Composable
fun RollSettingsDialog(
    stateHolder: RollSettingsStateHolder = RollSettingsStateHolder(Die(6)),
    onDismissRequest: () -> Unit = {}
) {
    val savedState = rememberSaveable { stateHolder.state }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {},
        text = { DialogContent(
            savedState.value,
            onDiceNumberChanged = { stateHolder.changeDiceNumber(it) },
            onModifierChanged = { stateHolder.changeModifier(it) },
            onAdvantageSettingChanged = {
                stateHolder.changeAdvantageSettings(it)
                if (it != AdvantageSetting.NORMAL) {
                    stateHolder.resetDiceNumber()
                }
            }
        ) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun DialogContent(
    settingsState: RollSettingsState,
    onDiceNumberChanged: (Int) -> Unit,
    onModifierChanged: (Int) -> Unit,
    onAdvantageSettingChanged: (AdvantageSetting) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RollDescription(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            settingsState
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            RollSetting(
                settingName = stringResource(id = R.string.roll_setting_dice_number),
                shouldDisableControls = settingsState.advantageSetting != AdvantageSetting.NORMAL,
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
        AdvantagesSettings(onAdvantageSettingChanged)
    }
}

@Composable
fun RollDescription(
    modifier: Modifier,
    settingsState: RollSettingsState
) {
    Text(
        text = "${settingsState.diceNumber}d${settingsState.die.sides} + ${settingsState.modifier}",
        modifier = modifier,
        style = MaterialTheme.typography.headlineLarge
    )
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
fun AdvantagesSettings(
    onAdvantageSettingChanged: (AdvantageSetting) -> Unit
) {
    AndroidView(
        factory = { View.inflate(it, R.layout.advantages_button, null) },
        update = {
            it.findViewById<MaterialButtonToggleGroup>(R.id.adv_setting_button).apply {
                addOnButtonCheckedListener { _, checkedId, _ ->
                    val currentSetting = when (checkedId) {
                        R.id.button_adv -> AdvantageSetting.ADVANTAGE
                        R.id.button_disadv -> AdvantageSetting.DISADVANTAGE
                        R.id.button_normal -> AdvantageSetting.NORMAL
                        else -> AdvantageSetting.NORMAL
                    }
                    onAdvantageSettingChanged(currentSetting)
                }
            }
        }
    )
}


@Composable
@Preview
fun RollSettingsDialogPreview() {
    DicePouchTheme {
        RollSettingsDialog()
    }
}


class RollSettingsStateHolder(die: Die) {

    val state = mutableStateOf(RollSettingsState(die))

    fun changeDiceNumber(change: Int) {
        state.value = state.value.copy(diceNumber = state.value.diceNumber + change)
    }

    fun changeModifier(change: Int) {
        state.value = state.value.copy(modifier = state.value.modifier + change)
    }

    fun changeAdvantageSettings(newValue: AdvantageSetting) {
        state.value = state.value.copy(advantageSetting = newValue)
    }

    fun resetDiceNumber() {
        state.value = state.value.copy(diceNumber = 1)
    }
}

@Parcelize
data class RollSettingsState(
    val die: Die,
    val diceNumber: Int = 1,
    val modifier: Int = 0,
    val advantageSetting: AdvantageSetting = AdvantageSetting.NORMAL
) : Parcelable


@Parcelize
enum class AdvantageSetting : Parcelable {
    ADVANTAGE, NORMAL, DISADVANTAGE;
}