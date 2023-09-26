package com.krystianrymonlipinski.dicepouch.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_NAME

@Entity(tableName = DICE_TABLE_NAME)
class DieEntity(
    @PrimaryKey @ColumnInfo(name = "timestamp_id") val timestampId: Long,
    @ColumnInfo(name = "sides") val sides: Int,
    @ColumnInfo(name = "sides_color_argb") val sidesColorArgb: Int,
    @ColumnInfo(name = "number_color_argb") val numberColorArgb: Int
)