package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Embedded
import androidx.room.Relation
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_TIMESTAMP
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_COLUMN_TIMESTAMP

data class ShortcutAndDie(
    @Embedded val shortcut: ShortcutEntity,
    @Relation(
        parentColumn = SHORTCUTS_TABLE_COLUMN_TIMESTAMP,
        entityColumn = DICE_TABLE_COLUMN_TIMESTAMP
    )
    val die: DieEntity
) {
}