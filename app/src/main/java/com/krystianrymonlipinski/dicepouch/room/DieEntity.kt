package com.krystianrymonlipinski.dicepouch.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_NUMBER_COLOR
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_SIDES
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_SIDES_COLOR
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_TIMESTAMP
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_NAME

@Entity(tableName = DICE_TABLE_NAME)
data class DieEntity(
    @PrimaryKey @ColumnInfo(name = DICE_TABLE_COLUMN_TIMESTAMP) val timestampId: Long,
    @ColumnInfo(name = DICE_TABLE_COLUMN_SIDES) val sides: Int,
    @ColumnInfo(name = DICE_TABLE_COLUMN_SIDES_COLOR) val sidesColorArgb: Int,
    @ColumnInfo(name = DICE_TABLE_COLUMN_NUMBER_COLOR) val numberColorArgb: Int
)