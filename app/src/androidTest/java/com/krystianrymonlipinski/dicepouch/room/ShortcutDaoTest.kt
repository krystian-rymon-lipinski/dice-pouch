package com.krystianrymonlipinski.dicepouch.room

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShortcutDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var shortcutDao: ShortcutDao
    private lateinit var dieDao: DieDao

    private val dieId: Long = 1L
    private val dieId2: Long = 2L
    private val shortcutExample1 = ShortcutEntity(
        timestampId = 0L,
        name = "a_name",
        diceNumber = 1,
        dieId = dieId,
        modifier = 10,
        mechanic = RollSetting.Mechanic.NORMAL.toString()
    )

    private val shortcutExample2 = ShortcutEntity(
    timestampId = 4L,
    name = "another_name",
    diceNumber = 4,
    dieId = dieId2,
    modifier = -2,
    mechanic = RollSetting.Mechanic.ADVANTAGE.toString()
    )

    private val shortcutExample1Updated = shortcutExample1.copy(
        name = "differentName",
        diceNumber = 4,
        modifier = 12,
        mechanic = RollSetting.Mechanic.ADVANTAGE.toString()
    )

    private val dieExample = DieEntity(
        timestampId = dieId,
        sides = 6,
        sidesColorArgb = Color.White.toArgb(),
        numberColorArgb = Color.Black.toArgb()
    )


    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        shortcutDao = db.shortcutDao()
        dieDao = db.dieDao()

        dieDao.add(dieExample)
    }

    @Test
    fun retrieveShortcutWithDie() = runTest {
        shortcutDao.add(shortcutExample1)
        val shortcutAndDieStored = dieDao.retrieveAllWithShortcuts().take(1).single()

        assertEquals(1, shortcutAndDieStored.size)
        assertEquals(shortcutExample1, shortcutAndDieStored[0].shortcut)
        assertEquals(dieExample, shortcutAndDieStored[0].die)
    }

    @Test
    fun addShortcutToDatabase() = runTest {
        shortcutDao.add(shortcutExample1)
        val shortcutsStored = dieDao.retrieveAllWithShortcuts().take(1).single()

        assertEquals(1, shortcutsStored.size)
        assertEquals(shortcutExample1, shortcutsStored[0].shortcut)
    }

    @Test
    fun deleteShortcutFromDatabase() = runTest {
        shortcutDao.apply {
            add(shortcutExample2)
            add(shortcutExample1)
            delete(shortcutExample2)
        }
        val shortcutsStored = dieDao.retrieveAllWithShortcuts().take(1).single()

        assertEquals(1, shortcutsStored.size)
        assertEquals(shortcutExample1, shortcutsStored[0].shortcut)
    }

    @Test
    fun updateShortcut() = runTest {
        shortcutDao.apply {
            add(shortcutExample1)
            update(shortcutExample1Updated)
        }
        val shortcutsStored = dieDao.retrieveAllWithShortcuts().take(1).single()

        assertEquals(1, shortcutsStored.size)
        assertEquals(shortcutExample1Updated, shortcutsStored[0].shortcut)
    }


}