package com.krystianrymonlipinski.dicepouch.hilt

import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppDataLayerModule {

    @Binds
    abstract fun bindDiceLocalDataSource(impl: DiceLocalDataSourceImpl) : DiceLocalDataSource

    @Binds
    abstract fun bindShortcutsDataSource(impl: ShortcutsLocalDataSourceImpl) : ShortcutsLocalDataSource

    @Binds
    abstract fun bindSetsDataSource(impl: SetsLocalDataSourceImpl) : SetsLocalDataSource

    @Binds
    abstract fun bindsSettingsDataSource(impl: SettingsLocalDataSourceImpl) : SettingsLocalDataSource
}