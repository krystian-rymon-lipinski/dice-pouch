package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortcutDao {

    @Transaction
    @Query("SELECT * FROM $SHORTCUTS_TABLE_NAME")
    fun retrieveAll() : Flow<List<ShortcutAndDie>>

    @Insert
    fun add(shortcut: ShortcutEntity)

    @Update
    fun update(shortcut: ShortcutEntity)

    @Delete
    fun delete(shortcut: ShortcutEntity)
}