package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DieDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dieDao: DieDao


    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dieDao = db.dieDao()
    }


    @Test
    fun addDieToDatabase() = runTest {
        val dieToAdd = DieEntity(timestampId = 1, sides = 6, sidesColorArgb = 0, numberColorArgb = 0)

        dieDao.add(dieToAdd)
        val diceStored = dieDao.retrieveAllWithShortcuts().take(1).single()

        assertEquals(1, diceStored.size)
        assertEquals(dieToAdd, diceStored[0].die)
    }

    @Test
    fun deleteDieFromDatabase() = runTest {
        val dieToSpare = DieEntity(timestampId = 1, sides = 6, sidesColorArgb = 0, numberColorArgb = 0)
        val dieToDelete = DieEntity(timestampId = 2, sides = 20, sidesColorArgb = 0, numberColorArgb = 0)

        dieDao.add(dieToDelete)
        dieDao.add(dieToSpare)
        dieDao.delete(dieToDelete)
        val diceStored = dieDao.retrieveAllWithShortcuts().take(1).single()

        assertEquals(1, diceStored.size)
        assertEquals(dieToSpare, diceStored[0].die)
    }


    @After
    fun tearDown() {
        db.close()
    }
}