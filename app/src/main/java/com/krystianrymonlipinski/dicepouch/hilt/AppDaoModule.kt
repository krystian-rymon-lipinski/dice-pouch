package com.krystianrymonlipinski.dicepouch.hilt

import com.krystianrymonlipinski.dicepouch.room.AppDatabase
import com.krystianrymonlipinski.dicepouch.room.DieDao
import com.krystianrymonlipinski.dicepouch.room.SetDao
import com.krystianrymonlipinski.dicepouch.room.ShortcutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AppDaoModule {

    @ViewModelScoped
    @Provides
    fun provideDiceDao(appDatabase: AppDatabase) : DieDao {
        return appDatabase.dieDao()
    }

    @ViewModelScoped
    @Provides
    fun provideShortcutsDao(appDatabase: AppDatabase) : ShortcutDao {
        return appDatabase.shortcutDao()
    }

    @ViewModelScoped
    @Provides
    fun provideSetsDao(appDatabase: AppDatabase) : SetDao {
        return appDatabase.setDao()
    }
}