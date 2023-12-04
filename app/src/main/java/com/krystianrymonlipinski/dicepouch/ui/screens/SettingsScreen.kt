package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import com.krystianrymonlipinski.dicepouch.ui.DicePouchTabRow
import com.krystianrymonlipinski.dicepouch.ui.TAB_SETTINGS
import com.krystianrymonlipinski.dicepouch.ui.components.SecondaryCaption
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import com.krystianrymonlipinski.dicepouch.viewmodels.MainActivityViewModel

@Composable
fun SettingsRoute(
    viewModel: MainActivityViewModel = hiltViewModel(),
    onTabClicked: (Int) -> Unit
) {

    SettingsScreen(
        onTabClicked = { tabIndex -> onTabClicked(tabIndex) },
        settingsScreenState = viewModel.retrieveSettings(),
        onSettingsChanged = { newSettings ->
            viewModel.saveSettings(newSettings)
        }
    )
}

@Composable
fun SettingsScreen(
    onTabClicked: (Int) -> Unit = {},
    settingsScreenState: RollingSettings = RollingSettings(),
    onSettingsChanged: (RollingSettings) -> Unit = {}
) {

    var currentSettings by rememberSaveable { mutableStateOf(settingsScreenState) }

    Scaffold { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
        ) {
            DicePouchTabRow(
                selectedTabIndex = TAB_SETTINGS,
                onTabClicked = { tabIndex -> onTabClicked(tabIndex) }
            )
            SettingsElementsLayout(
                savedSettings = currentSettings,
                onSoundSettingSwitched = { isOn ->
                    currentSettings = currentSettings.setIsSoundOn(isOn)
                    onSettingsChanged(currentSettings)
                },
                onThrowTimeChange = { newValue -> currentSettings = currentSettings.setSingleThrowTimeMillis(newValue) },
                onThrowTimeChangeFinished = { onSettingsChanged(currentSettings) },
                onThrowDelayTimeChange = { newValue -> currentSettings = currentSettings.setDelayBetweenThrowTimeMillis(newValue) },
                onThrowDelayTimeChangeFinished = { onSettingsChanged(currentSettings) },
                onAutocloseSettingChanged = { isOn ->
                    currentSettings = currentSettings.setIsRollPopupAutodismissOn(isOn)
                    onSettingsChanged(currentSettings)
                },
                onPopupDismissTimeChange = { newValue -> currentSettings = currentSettings.setRollPopupAutodismissTimeMillis(newValue) },
                onPopupDismissTimeChangeFinished = { onSettingsChanged(currentSettings) }
            )
        }
    }
}

@Composable
fun SettingsElementsLayout(
    savedSettings: RollingSettings,
    onSoundSettingSwitched: (Boolean) -> Unit,
    onThrowTimeChange: (Int) -> Unit,
    onThrowTimeChangeFinished: () -> Unit,
    onThrowDelayTimeChange: (Int) -> Unit,
    onThrowDelayTimeChangeFinished: () -> Unit,
    onAutocloseSettingChanged: (Boolean) -> Unit,
    onPopupDismissTimeChange: (Int) -> Unit,
    onPopupDismissTimeChangeFinished: () -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        SecondaryCaption(text = stringResource(id = R.string.rolling_dice_caption))
        Spacer(modifier = Modifier.height(16.dp))

        RollSoundSettingRow(
            isSoundOn = savedSettings.isSoundOn,
            onSoundSettingSwitched = onSoundSettingSwitched
        )
        Spacer(modifier = Modifier.height(16.dp))
        SingleThrowTimeSettingColumn(
            singleThrowTimeMillis = savedSettings.singleThrowTimeMillis,
            onThrowTimeChange = onThrowTimeChange,
            onThrowTimeChangeFinished = onThrowTimeChangeFinished
        )
        Spacer(modifier = Modifier.height(16.dp))
        ThrowDelaySettingRow(
            throwDelayTimeMillis = savedSettings.delayBetweenThrowsTimeMillis,
            onThrowDelayTimeChange = onThrowDelayTimeChange,
            onThrowDelayTimeChangeFinished = onThrowDelayTimeChangeFinished
        )
        Spacer(modifier = Modifier.height(16.dp))
        RollPopupClosingSettingRow(
            isPopupAutocloseOn = savedSettings.isRollPopupAutodismissOn,
            onAutocloseSettingChanged = onAutocloseSettingChanged,
            popupDismissTimeMillis = savedSettings.rollPopupAutodismissTimeMillis,
            onPopupDismissTimeChange = onPopupDismissTimeChange,
            onPopupDismissTimeChangeFinished = onPopupDismissTimeChangeFinished
        )
    }
}

@Composable
fun RollSoundSettingRow(
    isSoundOn: Boolean,
    onSoundSettingSwitched: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.setting_sound_caption),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
                .copy(fontWeight = FontWeight.Bold)
        )
        Switch(checked = isSoundOn, onCheckedChange = onSoundSettingSwitched)
    }
}

@Composable
fun SingleThrowTimeSettingColumn(
    singleThrowTimeMillis: Int,
    onThrowTimeChange: (Int) -> Unit,
    onThrowTimeChangeFinished: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(1f)) {
        Text(
            text = stringResource(id = R.string.setting_time_caption, singleThrowTimeMillis),
            style = MaterialTheme.typography.bodyMedium
                .copy(fontWeight = FontWeight.Bold)
        )
        Slider(
            value = singleThrowTimeMillis.toFloat(),
            onValueChange = { newValue -> onThrowTimeChange(newValue.toInt()) },
            onValueChangeFinished = onThrowTimeChangeFinished,
            valueRange = MIN_ROLL_TIME_MILLIS.rangeTo(MAX_ROLL_TIME_MILLIS),
            steps = ROLL_TIME_STEPS_NUMBER
        )
    }
}

@Composable
fun ThrowDelaySettingRow(
    throwDelayTimeMillis: Int,
    onThrowDelayTimeChange: (Int) -> Unit,
    onThrowDelayTimeChangeFinished: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(1f)) {
        Text(
            text = stringResource(id = R.string.setting_throw_delay_caption, throwDelayTimeMillis),
            style = MaterialTheme.typography.bodyMedium
                .copy(fontWeight = FontWeight.Bold)
        )
        Slider(
            value = throwDelayTimeMillis.toFloat(),
            onValueChange = { newValue -> onThrowDelayTimeChange(newValue.toInt()) },
            onValueChangeFinished = onThrowDelayTimeChangeFinished,
            valueRange = MIN_THROW_DELAY_TIME_MILLIS.rangeTo(MAX_THROW_DELAY_TIME_MILLIS),
            steps = THROW_DELAY_STEPS_NUMBER
        )
    }
}

@Composable
fun RollPopupClosingSettingRow(
    isPopupAutocloseOn: Boolean,
    onAutocloseSettingChanged: (Boolean) -> Unit,
    popupDismissTimeMillis: Int,
    onPopupDismissTimeChange: (Int) -> Unit,
    onPopupDismissTimeChangeFinished: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
           Text(
               modifier = Modifier.weight(1f),
               text = stringResource(id = R.string.setting_roll_popup_automatic_dismiss),
               style = MaterialTheme.typography.bodyMedium
                   .copy(fontWeight = FontWeight.Bold)
           )
           Switch(
               modifier = Modifier.testTag("popup_autodismiss_switch"),
               checked = isPopupAutocloseOn,
               onCheckedChange = onAutocloseSettingChanged
           )
        }
        Spacer(modifier = Modifier.height(2.dp))

        AnimatedVisibility(visible = isPopupAutocloseOn) {
            Column {
                Text(
                    text = stringResource(id = R.string.setting_roll_popup_dismiss_time, popupDismissTimeMillis),
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = popupDismissTimeMillis.toFloat(),
                    onValueChange = { newValue -> onPopupDismissTimeChange(newValue.toInt()) },
                    onValueChangeFinished = onPopupDismissTimeChangeFinished,
                    valueRange = MIN_POPUP_DISMISS_TIME_MILLIS.rangeTo(MAX_POPUP_DISMISS_TIME_MILLIS),
                    steps = POPUP_DISMISS_TIME_STEPS_NUMBER
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun SettingsScreenPreview() {
    DicePouchTheme {
        SettingsScreen()
    }
}

private const val MIN_ROLL_TIME_MILLIS = 200f
private const val MAX_ROLL_TIME_MILLIS = 3000f
private const val ROLL_TIME_STEP_MILLIS = 50f
private const val ROLL_TIME_STEPS_NUMBER = ((MAX_ROLL_TIME_MILLIS - MIN_ROLL_TIME_MILLIS) / ROLL_TIME_STEP_MILLIS).toInt() - 1

private const val MIN_THROW_DELAY_TIME_MILLIS = 50f
private const val MAX_THROW_DELAY_TIME_MILLIS = 2000f
private const val THROW_DELAY_STEP_MILLIS = 50f
private const val THROW_DELAY_STEPS_NUMBER = ((MAX_THROW_DELAY_TIME_MILLIS - MIN_THROW_DELAY_TIME_MILLIS) / THROW_DELAY_STEP_MILLIS).toInt() - 1

private const val MIN_POPUP_DISMISS_TIME_MILLIS = 500f
private const val MAX_POPUP_DISMISS_TIME_MILLIS = 3000f
private const val POPUP_DISMISS_TIME_STEP_MILLIS = 50f
private const val POPUP_DISMISS_TIME_STEPS_NUMBER = ((MAX_POPUP_DISMISS_TIME_MILLIS - MIN_POPUP_DISMISS_TIME_MILLIS) / POPUP_DISMISS_TIME_STEP_MILLIS).toInt() - 1