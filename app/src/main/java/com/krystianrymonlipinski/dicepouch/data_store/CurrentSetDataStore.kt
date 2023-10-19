package com.krystianrymonlipinski.dicepouch.data_store

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.currentSetDataStore by preferencesDataStore(name = "settings")

val CURRENT_SET_NAME_KEY = intPreferencesKey("current_set_id_key")
