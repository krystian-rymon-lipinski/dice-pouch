package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {

    @Query("SELECT * FROM ${AppDatabase.SETS_TABLE_NAME}")
    fun retrieveAll() : Flow<List<SetEntity>>

    @Transaction
    @Query("SELECT * FROM ${AppDatabase.SETS_TABLE_NAME} WHERE ${AppDatabase.SET_TABLE_COLUMN_ID} = :id")
    fun retrieveSetWithId(id: Int) : Flow<SetWithDice?>

    @Insert
    fun add(newSet: SetEntity)

    @Update
    fun update(set: SetEntity)

    @Delete
    fun delete(setToDelete: SetEntity)
}