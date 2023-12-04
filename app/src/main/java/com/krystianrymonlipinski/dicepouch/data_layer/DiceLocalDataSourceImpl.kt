package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.room.DieDao
import com.krystianrymonlipinski.dicepouch.room.DieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiceLocalDataSourceImpl @Inject constructor(
    private val dieDao: DieDao
) : DiceLocalDataSource {


    override suspend fun addNewDieToSet(setId: Int, die: Die) = withContext(Dispatchers.IO) {
        dieDao.add(convertToEntity(setId, die))
    }

    override suspend fun deleteDieFromSet(setId: Int, die: Die) = withContext(Dispatchers.IO) {
        dieDao.delete(convertToEntity(setId, die))
    }


    private fun convertToEntity(setId: Int, die: Die) : DieEntity {
        return DieEntity(
            die.timestampId,
            setId,
            die.sides
        )
    }

}