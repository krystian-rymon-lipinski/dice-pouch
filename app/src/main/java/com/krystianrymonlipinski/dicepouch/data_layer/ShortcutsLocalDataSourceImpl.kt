package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.ShortcutDao
import com.krystianrymonlipinski.dicepouch.room.ShortcutEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShortcutsLocalDataSourceImpl @Inject constructor(
    private val shortcutDao: ShortcutDao
) : ShortcutsLocalDataSource {


    override suspend fun addNewShortcutToSet(newShortcut: RollShortcut) = withContext(Dispatchers.IO) {
        shortcutDao.add(convertToEntity(newShortcut))
    }

    override suspend fun updateShortcut(shortcutToUpdate: RollShortcut) = withContext(Dispatchers.IO) {
        shortcutDao.update(convertToEntity(shortcutToUpdate))
    }

    override suspend fun deleteShortcutFromSet(shortcutToDelete: RollShortcut) = withContext(Dispatchers.IO) {
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
}