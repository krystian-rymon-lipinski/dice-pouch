package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Embedded
import androidx.room.Relation
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_COLUMN_TIMESTAMP
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.SHORTCUTS_TABLE_COLUMN_DIE_ID

data class ShortcutAndDie(
    @Embedded val die: DieEntity,
    @Relation(
        parentColumn = DICE_TABLE_COLUMN_TIMESTAMP,
        entityColumn = SHORTCUTS_TABLE_COLUMN_DIE_ID
    )
    val shortcut: ShortcutEntity,
) {
}