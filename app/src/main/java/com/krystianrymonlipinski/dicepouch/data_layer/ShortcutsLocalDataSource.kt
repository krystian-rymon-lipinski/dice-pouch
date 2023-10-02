package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import kotlinx.coroutines.flow.Flow

interface ShortcutsLocalDataSource {

    fun getShortcutsStream() : Flow<List<RollShortcut>>
    suspend fun addNewShortcutToSet(newShortcut: RollShortcut)
    suspend fun updateShortcut(shortcutToUpdate: RollShortcut)
    suspend fun deleteShortcutFromSet(shortcutToDelete: RollShortcut)
}