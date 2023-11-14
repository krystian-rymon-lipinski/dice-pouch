package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.Die

interface DiceLocalDataSource {

    suspend fun addNewDieToSet(setId: Int, die: Die)
    suspend fun deleteDieFromSet(setId: Int, die: Die)
}