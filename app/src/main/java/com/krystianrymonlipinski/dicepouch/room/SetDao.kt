package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {

    @Query("SELECT * FROM ${AppDatabase.SETS_TABLE_NAME}")
    fun retrieveAll() : Flow<List<SetEntity>>

    //TODO: retrieve chosen set by name; also ensure that all names are unique,
    // either by uniqueness of a column or an impossibility of adding another with the same name

    @Insert
    fun add(newSet: SetEntity)

    @Update
    fun update(set: SetEntity)

    @Delete
    fun delete(setToDelete: SetEntity)
}