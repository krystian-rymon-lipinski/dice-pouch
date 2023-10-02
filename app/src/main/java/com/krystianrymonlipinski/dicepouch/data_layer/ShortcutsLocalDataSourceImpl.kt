package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.compose.ui.graphics.Color
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.DieDao
import com.krystianrymonlipinski.dicepouch.room.ShortcutAndDie
import com.krystianrymonlipinski.dicepouch.room.ShortcutDao
import com.krystianrymonlipinski.dicepouch.room.ShortcutEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShortcutsLocalDataSourceImpl @Inject constructor(
    private val shortcutDao: ShortcutDao,
    private val dieDao: DieDao
) : ShortcutsLocalDataSource {

    override fun getShortcutsStream() : Flow<List<RollShortcut>> {
        return dieDao.retrieveAllWithShortcuts().map {
            it.map { shortcutAndDie -> convertFromEntity(shortcutAndDie) }
        }
    }

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

    private fun convertFromEntity(shortcutAndDie: ShortcutAndDie) : RollShortcut {
        val shortcut = shortcutAndDie.shortcut
        val die = shortcutAndDie.die

        return RollShortcut(
            timestampId = shortcut.timestampId,
            name = shortcutAndDie.shortcut.name,
            setting = RollSetting(
                die = Die(
                    sides = die.sides,
                    sideColor = Color(die.sidesColorArgb),
                    numberColor = Color(die.numberColorArgb),
                    timestampId = die.timestampId
                ),
                diceNumber = shortcut.diceNumber,
                modifier = shortcut.modifier,
                mechanic = RollSetting.Mechanic.fromString(shortcut.mechanic)
            )
        )
    }
}