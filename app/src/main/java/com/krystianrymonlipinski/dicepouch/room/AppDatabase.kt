package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dieDao(): DieDao


    companion object {
        const val DATABASE_NAME = "dice_pouch_database"
        const val DICE_TABLE_NAME = "dice_table"
    }
}