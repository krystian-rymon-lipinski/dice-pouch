package com.krystianrymonlipinski.dicepouch.data_layer

import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {

    fun retrieveCurrentSetId() : Flow<Int?>
    suspend fun changeCurrentSet(chosenId: Int)
}