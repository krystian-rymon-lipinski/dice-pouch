package com.krystianrymonlipinski.dicepouch.ui.dialogs

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

@Composable
fun RollSettingsDialog(
    die: Die = Die(6),
    onDismissRequest: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {},
        text = { DialogContent(
            die,
            onDiceNumberChanged = {  },
            onModifierChanged = {  },
            onAdvantageSettingChanged = { }
        ) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun DialogContent(
    die: Die,
    onDiceNumberChanged: (Int) -> Unit,
    onModifierChanged: (Int) -> Unit,
    onAdvantageSettingChanged: (AdvantageSetting) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "1d${die.sides}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineLarge
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            RollSetting(
                settingName = stringResource(id = R.string.roll_setting_dice_number),
                onIncrementClicked = { onDiceNumberChanged(1) },
                onDecrementClicked = { onDiceNumberChanged(-1) }
            )
            RollSetting(
                settingName = stringResource(id = R.string.roll_setting_modifier),
                onIncrementClicked = { onModifierChanged(1) },
                onDecrementClicked = { onModifierChanged(-1) }
            )
        }
        AdvantagesSettings(onAdvantageSettingChanged)
    }
}

@Composable
fun RollSetting(
    settingName: String,
    onIncrementClicked: () -> Unit,
    onDecrementClicked: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onDecrementClicked() }) {
            Icon(imageVector = Icons.Filled.Remove, contentDescription = "minus")
        }
        Text(text = settingName)
        IconButton(onClick = { onIncrementClicked() }) {
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
@Preview()
fun RollSettingsDialogPreview() {
    DicePouchTheme {
        RollSettingsDialog()
    }
}


enum class AdvantageSetting {
    ADVANTAGE, NORMAL, DISADVANTAGE
}