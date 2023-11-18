package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {

    fun retrieveCurrentSetId() : Flow<Int?>
    suspend fun changeCurrentSet(chosenId: Int)

    fun retrieveRollingSettings() : Flow<RollingSettings>
    suspend fun saveRollingSettings(rollingSettings: RollingSettings)
}