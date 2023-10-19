package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.krystianrymonlipinski.dicepouch.data_store.CURRENT_SET_NAME_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsLocalDataSourceImpl @Inject constructor(
    private val currentSetDataStore: DataStore<Preferences>
) : SettingsLocalDataSource {

    override fun retrieveCurrentSetName() : Flow<Int?> {
        return currentSetDataStore.data.map { preferences ->
            preferences[CURRENT_SET_NAME_KEY]
        }
    }

    override suspend fun changeCurrentSet(chosenId: Int) {
        currentSetDataStore.edit { preferences ->
            preferences[CURRENT_SET_NAME_KEY] = chosenId
        }
    }

}