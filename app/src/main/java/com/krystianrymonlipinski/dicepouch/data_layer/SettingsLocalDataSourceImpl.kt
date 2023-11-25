package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.krystianrymonlipinski.dicepouch.data_store.CURRENT_SET_ID_KEY
import com.krystianrymonlipinski.dicepouch.data_store.DELAY_BETWEEN_THROWS_MILLIS_KEY
import com.krystianrymonlipinski.dicepouch.data_store.IS_ROLL_POPUP_AUTODISMISS_ON_KEY
import com.krystianrymonlipinski.dicepouch.data_store.IS_SOUND_ON_KEY
import com.krystianrymonlipinski.dicepouch.data_store.ROLL_POPUP_AUTODISMISS_TIME_MILLIS_KEY
import com.krystianrymonlipinski.dicepouch.data_store.SINGLE_THROW_TIME_MILLIS_KEY
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsLocalDataSourceImpl @Inject constructor(
    private val appSettingsDataStore: DataStore<Preferences>
) : SettingsLocalDataSource {

    override fun retrieveCurrentSetId() : Flow<Int?> {
        return appSettingsDataStore.data.map { preferences ->
            preferences[CURRENT_SET_ID_KEY]
        }
    }

    override suspend fun changeCurrentSet(chosenId: Int) { withContext(Dispatchers.IO) {
        appSettingsDataStore.edit { preferences ->
            preferences[CURRENT_SET_ID_KEY] = chosenId
        } }
    }

    override fun retrieveRollingSettings() : Flow<RollingSettings> {
        return appSettingsDataStore.data.map { preferences ->
            RollingSettings(
                isSoundOn = preferences[IS_SOUND_ON_KEY] ?: false,
                singleThrowTimeMillis = preferences[SINGLE_THROW_TIME_MILLIS_KEY] ?: DEFAULT_THROW_TIME_MILLIS,
                delayBetweenThrowsTimeMillis = preferences[DELAY_BETWEEN_THROWS_MILLIS_KEY] ?: DEFAULT_THROW_DELAY_TIME_MILLIS,
                isRollPopupAutodismissOn = preferences[IS_ROLL_POPUP_AUTODISMISS_ON_KEY] ?: false,
                rollPopupAutodismissTimeMillis = preferences[ROLL_POPUP_AUTODISMISS_TIME_MILLIS_KEY] ?: DEFAULT_POPUP_DISMISS_TIME_MILLIS
            )

        }
    }

    override suspend fun saveRollingSettings(rollingSettings: RollingSettings) { withContext(Dispatchers.IO) {
        appSettingsDataStore.edit { preferences ->
            preferences[IS_SOUND_ON_KEY] = rollingSettings.isSoundOn
            preferences[SINGLE_THROW_TIME_MILLIS_KEY] = rollingSettings.singleThrowTimeMillis
            preferences[DELAY_BETWEEN_THROWS_MILLIS_KEY] = rollingSettings.delayBetweenThrowsTimeMillis
            preferences[IS_ROLL_POPUP_AUTODISMISS_ON_KEY] = rollingSettings.isRollPopupAutodismissOn
            preferences[ROLL_POPUP_AUTODISMISS_TIME_MILLIS_KEY] = rollingSettings.rollPopupAutodismissTimeMillis
        } }
    }

    companion object {
        private const val DEFAULT_THROW_TIME_MILLIS = 1000
        private const val DEFAULT_THROW_DELAY_TIME_MILLIS = 500
        private const val DEFAULT_POPUP_DISMISS_TIME_MILLIS = 1000
    }

}