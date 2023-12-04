package com.krystianrymonlipinski.dicepouch.room

import com.krystianrymonlipinski.dicepouch.model.RollSetting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShortcutDaoTest : BaseDaoTest() {

    @Before
    fun setUpDiceSet() {
        setDao.add(SetEntity(id = setId, name = "a_set",
            diceSideColorArgb = 0, diceNumberColorArgb = 10))
    }


    private val dieId: Long = 1L
    private val shortcutsExample1 = listOf(ShortcutEntity(
        timestampId = 0L,
        name = "a_name",
        diceNumber = 1,
        dieId = dieId,
        modifier = 10,
        mechanic = RollSetting.Mechanic.NORMAL.toString()
    ))

    private val shortcutsExample2 = listOf(ShortcutEntity(
    timestampId = 4L,
    name = "another_name",
    diceNumber = 4,
    dieId = dieId,
    modifier = -2,
    mechanic = RollSetting.Mechanic.ADVANTAGE.toString()
    ))

    private val shortcutsExample1Updated = listOf(shortcutsExample1[0].copy(
        name = "differentName",
        diceNumber = 4,
        modifier = 12,
        mechanic = RollSetting.Mechanic.ADVANTAGE.toString()
    ))

    private val dieExample = DieEntity(
        timestampId = dieId,
        setId = setId,
        sides = 6
    )


    @Before
    fun setUpDao() {
        shortcutDao = db.shortcutDao()
        dieDao = db.dieDao()

        dieDao.add(dieExample)
    }

    @Test
    fun addShortcutToDatabase() = runTest {
        shortcutDao.add(shortcutsExample1[0])
        val shortcutsStored = shortcutDao.retrieveAll().take(1).single()

        assertEquals(1, shortcutsStored.size)
        assertEquals(shortcutsExample1, shortcutsStored)
    }

    @Test
    fun deleteShortcutFromDatabase() = runTest {
        shortcutDao.apply {
            add(shortcutsExample2[0])
            add(shortcutsExample1[0])
            delete(shortcutsExample2[0])
        }
        val diceStored = dieDao.retrieveAllWithShortcuts().take(1).single()
        val shortcutsStored = shortcutDao.retrieveAll().take(1).single()

        assertEquals(1, diceStored.size)
        assertEquals(1, shortcutsStored.size)
        assertEquals(shortcutsExample1, shortcutsStored)
    }

    @Test
    fun updateShortcut() = runTest {
        shortcutDao.apply {
            add(shortcutsExample1[0])
            update(shortcutsExample1Updated[0])
        }
        val shortcutsStored = shortcutDao.retrieveAll().take(1).single()

        assertEquals(1, shortcutsStored.size)
        assertEquals(shortcutsExample1Updated, shortcutsStored)
    }


}