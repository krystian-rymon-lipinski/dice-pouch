package com.krystianrymonlipinski.dicepouch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSource
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : ViewModel() {


    fun saveSettings(settings: RollingSettings) {
        viewModelScope.launch {
            settingsLocalDataSource.saveRollingSettings(settings)
        }
    }

    fun retrieveSettings() : RollingSettings {
        var settings = RollingSettings()
        viewModelScope.launch {
            settings = settingsLocalDataSource.retrieveRollingSettings().take(1).single()
        }
        return settings
    }
}