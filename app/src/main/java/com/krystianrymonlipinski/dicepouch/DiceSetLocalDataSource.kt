package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.room.DieDao
import com.krystianrymonlipinski.dicepouch.room.DieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiceSetLocalDataSource @Inject constructor(
    private val dieDao: DieDao
) {

    fun getDiceStream() : Flow<List<Die>> {
        return dieDao.retrieveAll().map {
            it.map { entity -> convertFromEntity(entity) }
        }
    }

    suspend fun addNewDieToSet(die: Die) = withContext(Dispatchers.IO) {
        dieDao.add(convertToEntity(die))
    }

    suspend fun deleteDieFromSet(die: Die) = withContext(Dispatchers.IO) {
        dieDao.delete(convertToEntity(die))
    }



    private fun convertToEntity(die: Die) : DieEntity {
        return DieEntity(
            die.timestampId,
            die.sides,
            die.sideColor.toArgb(),
            die.numberColor.toArgb()
        )
    }

    private fun convertFromEntity(dieEntity: DieEntity) : Die {
        return Die(
            dieEntity.sides,
            Color(dieEntity.sidesColorArgb),
            Color(dieEntity.numberColorArgb),
            dieEntity.timestampId
        )
    }
}