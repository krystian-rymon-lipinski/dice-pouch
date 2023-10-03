package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.krystianrymonlipinski.dicepouch.room.AppDatabase.Companion.DICE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface DieDao {

    @Query("SELECT * FROM $DICE_TABLE_NAME")
    fun retrieveAll() : Flow<List<DieEntity>>

    @Transaction
    @Query("SELECT * FROM $DICE_TABLE_NAME")
    fun retrieveAllWithShortcuts() : Flow<List<DieWithShortcuts>>

    @Insert
    fun add(die: DieEntity)

    @Delete
    fun delete(die: DieEntity)
}