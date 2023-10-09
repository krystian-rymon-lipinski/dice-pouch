package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.DieDao
import com.krystianrymonlipinski.dicepouch.room.DieEntity
import com.krystianrymonlipinski.dicepouch.room.DieWithShortcuts
import com.krystianrymonlipinski.dicepouch.room.ShortcutEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiceLocalDataSourceImpl @Inject constructor(
    private val dieDao: DieDao
) : DiceLocalDataSource {

    private val diceWithShortcutsStream: Flow<List<DieWithShortcuts>> = dieDao.retrieveAllWithShortcuts()

    override fun getDiceStream() : Flow<List<Die>> {
        return diceWithShortcutsStream.map {
            it.map { entity -> convertFromEntity(entity.die) }
        }
    }

    override fun getShortcutsStream() : Flow<List<RollShortcut>> {
        return diceWithShortcutsStream.map { list ->
            list.flatMap { entity ->
                entity.shortcuts.map { shortcut ->
                    convertToShortcutFromRelatedEntities(shortcut, entity.die)
                }
            }.sortedBy { it.timestampId }
        }
    }

    override suspend fun addNewDieToSet(die: Die) = withContext(Dispatchers.IO) {
        dieDao.add(convertToEntity(die))
    }

    override suspend fun deleteDieFromSet(die: Die) = withContext(Dispatchers.IO) {
        dieDao.delete(convertToEntity(die))
    }



    private fun convertToEntity(die: Die) : DieEntity {
        return DieEntity(
            die.timestampId,
            0, //TODO: pass correct setId
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

    private fun convertToShortcutFromRelatedEntities(
        shortcut: ShortcutEntity,
        dieEntity: DieEntity
    ) : RollShortcut {
        return RollShortcut(
            timestampId = shortcut.timestampId,
            name = shortcut.name,
            setting = RollSetting(
                die = convertFromEntity(dieEntity),
                diceNumber = shortcut.diceNumber,
                modifier = shortcut.modifier,
                mechanic = RollSetting.Mechanic.fromString(shortcut.mechanic)
            )
        )
    }
}