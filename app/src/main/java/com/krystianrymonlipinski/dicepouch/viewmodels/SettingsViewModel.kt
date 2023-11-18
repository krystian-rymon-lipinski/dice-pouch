package com.krystianrymonlipinski.dicepouch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSource
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : ViewModel() {

    val rollingSettingsStream = settingsLocalDataSource.retrieveRollingSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RollingSettings()
        )

    fun saveSettings(rollingSettings: RollingSettings) {
        viewModelScope.launch {
            Timber.d("HERE; view mode save settings")
            settingsLocalDataSource.saveRollingSettings(rollingSettings)
        }
    }
}