package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface ShortcutDao {

    @Insert
    fun add(shortcut: ShortcutEntity)

    @Update
    fun update(shortcut: ShortcutEntity)

    @Delete
    fun delete(shortcut: ShortcutEntity)
}