package com.krystianrymonlipinski.dicepouch.hilt

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.krystianrymonlipinski.dicepouch.data_store.currentSetDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DataStoreModule {

    @ViewModelScoped
    @Provides
    fun provideCurrentSetDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return context.currentSetDataStore
    }

}