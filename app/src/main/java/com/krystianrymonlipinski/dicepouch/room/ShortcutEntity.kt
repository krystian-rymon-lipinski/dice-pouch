package com.krystianrymonlipinski.dicepouch.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_COLUMN_DICE_NUMBER
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_COLUMN_DIE_ID
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_COLUMN_MECHANIC
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_COLUMN_MODIFIER
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_COLUMN_NAME
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_COLUMN_TIMESTAMP
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_NAME

@Entity(tableName = SHORTCUTS_TABLE_NAME)
data class ShortcutEntity(
    @PrimaryKey @ColumnInfo(name = SHORTCUTS_TABLE_COLUMN_TIMESTAMP) val timestampId: Long,
    @ColumnInfo(name = SHORTCUTS_TABLE_COLUMN_NAME) val name: String,
    @ColumnInfo(name = SHORTCUTS_TABLE_COLUMN_DICE_NUMBER) val diceNumber: Int,
    @ColumnInfo(name = SHORTCUTS_TABLE_COLUMN_DIE_ID) val dieId: Long,
    @ColumnInfo(name = SHORTCUTS_TABLE_COLUMN_MODIFIER) val modifier: Int,
    @ColumnInfo(name = SHORTCUTS_TABLE_COLUMN_MECHANIC) val mechanic: String
) {
}