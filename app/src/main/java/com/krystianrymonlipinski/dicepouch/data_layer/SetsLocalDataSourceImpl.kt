package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.DieEntity
import com.krystianrymonlipinski.dicepouch.room.SetDao
import com.krystianrymonlipinski.dicepouch.room.SetEntity
import com.krystianrymonlipinski.dicepouch.room.SetWithDice
import com.krystianrymonlipinski.dicepouch.room.ShortcutEntity
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

    override fun retrieveSetWithId(id: Int): Flow<DiceSet?> {
        return setDao.retrieveSetWithId(id).map { entity ->
            convertFromEntityWithDice(entity)
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
            id = set.id,
            name = set.name,
            diceSideColorArgb = set.diceColor.toArgb(),
            diceNumberColorArgb = set.numbersColor.toArgb()
        )
    }

    private fun convertFromEntity(entity: SetEntity) : DiceSetInfo {
        return DiceSetInfo(
            id = entity.id,
            name = entity.name,
            diceColor = Color(entity.diceSideColorArgb),
            numbersColor = Color(entity.diceNumberColorArgb)
        )
    }


    private fun convertFromEntityWithDice(entity: SetWithDice?) : DiceSet? {
        return entity?.let { entityToConvert ->
            DiceSet(
                info = DiceSetInfo(
                    id = entityToConvert.set.id,
                    name = entityToConvert.set.name,
                    diceColor = Color(entityToConvert.set.diceSideColorArgb),
                    numbersColor = Color(entityToConvert.set.diceNumberColorArgb)),
                dice = entityToConvert.diceWithShortcuts.map { convertToDieFromEntity(it.die) },
                shortcuts = entityToConvert.diceWithShortcuts.flatMap { dieWithShortcuts ->
                    dieWithShortcuts.shortcuts.map { shortcut ->
                        convertToShortcutFromRelatedEntities(
                            shortcut = shortcut,
                            dieEntity = dieWithShortcuts.die
                        )
                    }
                }
            )
        }
    }

    private fun convertToDieFromEntity(dieEntity: DieEntity) : Die {
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
                die = convertToDieFromEntity(dieEntity),
                diceNumber = shortcut.diceNumber,
                modifier = shortcut.modifier,
                mechanic = RollSetting.Mechanic.fromString(shortcut.mechanic)
            )
        )
    }

}