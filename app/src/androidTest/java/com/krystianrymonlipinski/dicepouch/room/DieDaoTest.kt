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
    private lateinit var shortcutsDao: ShortcutDao


    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dieDao = db.dieDao()
        shortcutsDao = db.shortcutDao()
    }


    @Test
    fun addDieToDatabase() = runTest {
        val dieToAdd = DieEntity(timestampId = 1, sides = 6, sidesColorArgb = 0, numberColorArgb = 0)

        dieDao.add(dieToAdd)
        val diceStored = dieDao.retrieveAllWithShortcuts().take(1).single()

        assertEquals(1, diceStored.size)
        assertEquals(dieToAdd, diceStored[0].die)
        assertEquals(emptyList<ShortcutEntity>(), diceStored[0].shortcuts)
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

    @Test
    fun retrieveDie_withShortcuts() = runTest {
        val dieId = 1L
        val dieEntity = DieEntity(timestampId = dieId, sides = 4, sidesColorArgb = 0, numberColorArgb = 0)
        val shortcutEntity1 = ShortcutEntity(timestampId = 12L, name = "a_name", diceNumber = 2, dieId = dieId, modifier = -1, mechanic = "NORMAL")
        val shortcutEntity2 = ShortcutEntity(timestampId = 13L, name = "b_name", diceNumber = 1, dieId = dieId, modifier = 0, mechanic = "NORMAL")

        dieDao.add(dieEntity)
        shortcutsDao.add(shortcutEntity1)
        shortcutsDao.add(shortcutEntity2)

        val diceStored = dieDao.retrieveAllWithShortcuts().take(1).single()

        assertEquals(1, diceStored.size)
        assertEquals(dieEntity, diceStored[0].die)
        assertEquals(2, diceStored[0].shortcuts.size)
        assertEquals(listOf(shortcutEntity1, shortcutEntity2), diceStored[0].shortcuts)

    }



    @After
    fun tearDown() {
        db.close()
    }
}