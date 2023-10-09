package com.krystianrymonlipinski.dicepouch.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_NUMBER_COLOR
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_SET_ID
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_SIDES
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_SIDES_COLOR
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_TIMESTAMP_ID
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_NAME
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SET_TABLE_COLUMN_ID

@Entity(
    tableName = DICE_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = SetEntity::class,
        parentColumns = [SET_TABLE_COLUMN_ID],
        childColumns = [DICE_TABLE_COLUMN_SET_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DieEntity(
    @PrimaryKey @ColumnInfo(name = DICE_TABLE_COLUMN_TIMESTAMP_ID) val timestampId: Long,
    @ColumnInfo(name = DICE_TABLE_COLUMN_SET_ID) val setId: Int,
    @ColumnInfo(name = DICE_TABLE_COLUMN_SIDES) val sides: Int,
    @ColumnInfo(name = DICE_TABLE_COLUMN_SIDES_COLOR) val sidesColorArgb: Int,
    @ColumnInfo(name = DICE_TABLE_COLUMN_NUMBER_COLOR) val numberColorArgb: Int
)