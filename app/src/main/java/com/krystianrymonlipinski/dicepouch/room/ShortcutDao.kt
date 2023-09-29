package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortcutDao {

    @Query("SELECT * FROM $SHORTCUTS_TABLE_NAME")
    fun retrieveAll() : Flow<List<RollShortcut>>

    @Insert
    fun add(shortcut: RollShortcut)

    @Update
    fun update(shortcut: RollShortcut)

    @Delete
    fun delete(shortcut: RollShortcut)
}