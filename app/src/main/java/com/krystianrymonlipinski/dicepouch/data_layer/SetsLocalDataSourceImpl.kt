package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.room.SetDao
import com.krystianrymonlipinski.dicepouch.room.SetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetsLocalDataSourceImpl @Inject constructor(
    private val setDao: SetDao
) : SetsLocalDataSource {

    override fun retrieveAllSetsInfo(): Flow<List<DiceSetInfo>> {
        return setDao.retrieveAll().map {
            it.map { entity -> convertFromEntity(entity) }
        }
    }

    override suspend fun addDiceSet(set: DiceSetInfo) = withContext(Dispatchers.IO) {
        setDao.add(convertToEntity(set))
    }

    override suspend fun deleteDiceSet(set: DiceSetInfo) = withContext(Dispatchers.IO) {
        setDao.delete(convertToEntity(set))
    }

    private fun convertToEntity(set: DiceSetInfo) : SetEntity {
        return SetEntity(
            name = set.name,
            diceSideColorArgb = set.diceColor.toArgb(),
            diceNumberColorArgb = set.numbersColor.toArgb()
        )
    }

    private fun convertFromEntity(entity: SetEntity) : DiceSetInfo {
        return DiceSetInfo(
            name = entity.name,
            diceColor = Color(entity.diceSideColorArgb),
            numbersColor = Color(entity.diceNumberColorArgb)
        )
    }


}