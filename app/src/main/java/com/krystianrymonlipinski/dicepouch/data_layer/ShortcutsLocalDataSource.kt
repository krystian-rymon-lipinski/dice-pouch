package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.ShortcutDao
import com.krystianrymonlipinski.dicepouch.room.ShortcutEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShortcutsLocalDataSource @Inject constructor(
    private val shortcutDao: ShortcutDao
) {

    fun getShortcutsStream() : Flow<List<RollShortcut>> {
        return shortcutDao.retrieveAll().map {
            it.map { entity -> convertFromEntity(entity) }
        }
    }

    suspend fun addNewShortcutToSet(newShortcut: RollShortcut) = withContext(Dispatchers.IO) {
        shortcutDao.add(convertToEntity(newShortcut))
    }

    suspend fun updateShortcut(shortcutToUpdate: RollShortcut) = withContext(Dispatchers.IO) {
        shortcutDao.update(convertToEntity(shortcutToUpdate))
    }

    suspend fun deleteShortcutFromSet(shortcutToDelete: RollShortcut) = withContext(Dispatchers.IO) {
        shortcutDao.delete(convertToEntity(shortcutToDelete))
    }


    private fun convertToEntity(shortcut: RollShortcut) : ShortcutEntity {
        return ShortcutEntity(
            timestampId = shortcut.timestampId,
            name = shortcut.name,
            diceNumber = shortcut.setting.diceNumber,
            dieId = shortcut.setting.die.timestampId,
            modifier = shortcut.setting.modifier,
            mechanic = shortcut.setting.mechanic.toString()
        )
    }

    private fun convertFromEntity(entity: ShortcutEntity) : RollShortcut {
        return RollShortcut(
            timestampId = entity.timestampId,
            name = entity.name,
            setting = RollSetting(
                die = Die(6), //TODO: retrieve die based on its id from the dice table
                diceNumber = entity.diceNumber,
                modifier = entity.modifier,
                mechanic = RollSetting.Mechanic.fromString(entity.mechanic)
            )
        )
    }
}