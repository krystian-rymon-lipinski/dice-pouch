package com.krystianrymonlipinski.dicepouch.data_store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.appSettingsDataStore by preferencesDataStore(name = "settings")

val CURRENT_SET_ID_KEY = intPreferencesKey("current_set_id_key")
val IS_SOUND_ON_KEY = booleanPreferencesKey("is_sound_on_key")
val SINGLE_THROW_TIME_MILLIS_KEY = intPreferencesKey("single_throw_time_millis_key")
val DELAY_BETWEEN_THROWS_MILLIS_KEY = intPreferencesKey("delay_between_throws_millis_key")
val IS_ROLL_POPUP_AUTODISMISS_ON_KEY = booleanPreferencesKey("is_roll_popup_autodismiss_on_key")
val ROLL_POPUP_AUTODISMISS_TIME_MILLIS_KEY = intPreferencesKey("roll_popup_autodismiss_time_millis_key")
