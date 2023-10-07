package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.RollShortcut

interface ShortcutsLocalDataSource {

    suspend fun addNewShortcutToSet(newShortcut: RollShortcut)
    suspend fun updateShortcut(shortcutToUpdate: RollShortcut)
    suspend fun deleteShortcutFromSet(shortcutToDelete: RollShortcut)
}