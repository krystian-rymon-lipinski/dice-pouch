package com.krystianrymonlipinski.dicepouch.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [DieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dieDao(): DieDao
    abstract fun shortcutDao() : ShortcutDao


    companion object {
        val databaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                //TODO: prepopulate database within coroutine
                prepopulateDatabase(db)
            }
        }

        private fun prepopulateDatabase(db: SupportSQLiteDatabase) {
            defaultDice.forEach {
                val contentValues = ContentValues(defaultDice.size).apply {
                    put(DICE_TABLE_COLUMN_TIMESTAMP, it.timestampId)
                    put(DICE_TABLE_COLUMN_SIDES, it.sides)
                    put(DICE_TABLE_COLUMN_SIDES_COLOR, it.sidesColorArgb)
                    put(DICE_TABLE_COLUMN_NUMBER_COLOR, it.numberColorArgb)
                }
                db.insert(table = DICE_TABLE_NAME, conflictAlgorithm = CONFLICT_REPLACE, values = contentValues)
            }
        }

        private val defaultDice = listOf(
            DieEntity(timestampId = 1L, sides = 4, sidesColorArgb = Color.White.toArgb(), numberColorArgb = Color.Black.toArgb()),
            DieEntity(timestampId = 2L, sides = 6, sidesColorArgb = Color.White.toArgb(), numberColorArgb = Color.Black.toArgb()),
            DieEntity(timestampId = 3L, sides = 8, sidesColorArgb = Color.White.toArgb(), numberColorArgb = Color.Black.toArgb()),
            DieEntity(timestampId = 4L, sides = 10, sidesColorArgb = Color.White.toArgb(), numberColorArgb = Color.Black.toArgb()),
            DieEntity(timestampId = 5L, sides = 12, sidesColorArgb = Color.White.toArgb(), numberColorArgb = Color.Black.toArgb()),
            DieEntity(timestampId = 6L, sides = 20, sidesColorArgb = Color.White.toArgb(), numberColorArgb = Color.Black.toArgb()),
        )

        const val DATABASE_NAME = "dice_pouch_database"
        const val DICE_TABLE_NAME = "dice_table"
        const val SHORTCUTS_TABLE_NAME = "shortcuts_table"

        const val DICE_TABLE_COLUMN_TIMESTAMP = "dice_timestamp_id"
        const val DICE_TABLE_COLUMN_SIDES = "sides"
        const val DICE_TABLE_COLUMN_SIDES_COLOR = "sides_color_argb"
        const val DICE_TABLE_COLUMN_NUMBER_COLOR = "number_color_argb"

        const val SHORTCUTS_TABLE_COLUMN_TIMESTAMP = "shortcut_timestamp_id"
        const val SHORTCUTS_TABLE_COLUMN_NAME = "name"
        const val SHORTCUTS_TABLE_COLUMN_DICE_NUMBER = "dice_number"
        const val SHORTCUTS_TABLE_COLUMN_DIE_ID = "die_id"
        const val SHORTCUTS_TABLE_COLUMN_MODIFIER = "modifier"
        const val SHORTCUTS_TABLE_COLUMN_MECHANIC = "mechanic"
    }
}