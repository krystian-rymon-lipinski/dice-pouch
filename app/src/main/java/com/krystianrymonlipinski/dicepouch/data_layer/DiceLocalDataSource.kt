package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.Die
import kotlinx.coroutines.flow.Flow

interface DiceLocalDataSource {

    fun getDiceStream() : Flow<List<Die>>
    suspend fun addNewDieToSet(die: Die)
    suspend fun deleteDieFromSet(die: Die)
}