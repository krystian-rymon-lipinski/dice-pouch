package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Embedded
import androidx.room.Relation

data class SetWithDice(
    @Embedded val set: SetEntity,
    @Relation(
        entity = DieEntity::class,
        parentColumn = AppDatabase.SET_TABLE_COLUMN_ID,
        entityColumn = AppDatabase.DICE_TABLE_COLUMN_SET_ID
    ) val diceWithShortcuts: List<DieWithShortcuts>
)