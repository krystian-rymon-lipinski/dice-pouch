package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import kotlinx.coroutines.flow.Flow

interface DiceLocalDataSource {

    fun getDiceStream() : Flow<List<Die>>
    fun getShortcutsStream() : Flow<List<RollShortcut>>
    suspend fun addNewDieToSet(die: Die)
    suspend fun deleteDieFromSet(die: Die)
}