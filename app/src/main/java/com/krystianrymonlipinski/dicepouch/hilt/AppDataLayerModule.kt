package com.krystianrymonlipinski.dicepouch.hilt

import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSourceImpl
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
}