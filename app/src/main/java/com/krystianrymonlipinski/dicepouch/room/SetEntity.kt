package com.krystianrymonlipinski.dicepouch.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AppDatabase.SETS_TABLE_NAME)
data class SetEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = AppDatabase.SET_TABLE_COLUMN_ID) val id: Int = 0,
    @ColumnInfo(name = AppDatabase.SET_TABLE_COLUMN_NAME) val name: String,
    @ColumnInfo(name = AppDatabase.SET_TABLE_COLUMN_DICE_SIDE_COLOR) val diceSideColorArgb: Int,
    @ColumnInfo(name = AppDatabase.SET_TABLE_COLUMN_DICE_NUMBER_COLOR) val diceNumberColorArgb: Int
)