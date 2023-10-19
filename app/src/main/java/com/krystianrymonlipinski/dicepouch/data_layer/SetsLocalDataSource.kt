package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import kotlinx.coroutines.flow.Flow

interface SetsLocalDataSource {

    fun retrieveAllSetsInfo() : Flow<List<DiceSetInfo>>
    fun retrieveSetWithId(id: Int) : Flow<DiceSet>
    suspend fun addDiceSet(set: DiceSetInfo)
    suspend fun deleteDiceSet(set: DiceSetInfo)
}