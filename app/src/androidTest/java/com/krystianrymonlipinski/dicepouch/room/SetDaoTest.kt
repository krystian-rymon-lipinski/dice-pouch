package com.krystianrymonlipinski.dicepouch.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetDaoTest : BaseDaoTest() {


    @Test
    fun checkInitialDatabaseSetup() = runTest {
        db = Room.inMemoryDatabaseBuilder( // overwrite base setup to add database callback
            context = ApplicationProvider.getApplicationContext(),
            klass = AppDatabase::class.java
        )
            .addCallback(AppDatabase.databaseCallback)
            .build()
        setDao = db.setDao()

        val retrievedSets = setDao.retrieveAll().take(1).single()

        assertEquals(1, retrievedSets.size)
        val set = retrievedSets[0]

        val setDetails = setDao.retrieveSetWithId(set.id).take(1).singleOrNull()
        setDetails?.let {
            assertEquals("Basic set", it.set.name)
            assertEquals(6, it.diceWithShortcuts.size)
            for (die in it.diceWithShortcuts) {
                if (die.die.sides != 20) {
                    assertEquals(0, die.shortcuts.size)
                } else {
                    assertEquals(1, die.shortcuts.size)
                    assertEquals("1d20 + 4 (A)", die.shortcuts[0].name)
                }
            }
        } ?: fail()
    }

    @Test
    fun addDiceSet() = runTest {
        val diceSetToAdd1 = SetEntity(name = "a_name", diceSideColorArgb = 0, diceNumberColorArgb = 0)
        val diceSetToAdd2 = SetEntity(name = "b_name", diceSideColorArgb = 12, diceNumberColorArgb = 17)
        val diceSetAfterAdding1 = diceSetToAdd1.copy(id = 1) // check for auto increment
        val diceSetAfterAdding2 = diceSetToAdd2.copy(id = 2)

        setDao.add(diceSetToAdd1)
        setDao.add(diceSetToAdd2)

        val retrievedSets = setDao.retrieveAll().take(1).single()
        assertEquals(2, retrievedSets.size)
        assertEquals(diceSetAfterAdding1, retrievedSets[0])
        assertEquals(diceSetAfterAdding2, retrievedSets[1])
    }

    @Test
    fun deleteDiceSet() = runTest {
        val diceSetToAdd1 = SetEntity(name = "a_name", diceSideColorArgb = 0, diceNumberColorArgb = 0)
        val diceSetToAdd2 = SetEntity(name = "b_name", diceSideColorArgb = 12, diceNumberColorArgb = 17)
        val diceSetToDelete = diceSetToAdd1.copy(id = 1)
        val diceSetToSpare = diceSetToAdd2.copy(id = 2)

        setDao.apply {
            add(diceSetToAdd1)
            add(diceSetToAdd2)
            delete(diceSetToDelete)
        }

        val retrievedSets = setDao.retrieveAll().take(1).single()
        assertEquals(1, retrievedSets.size)
        assertEquals(diceSetToSpare, retrievedSets[0])
    }

    @Test
    fun updateDiceSet() = runTest {
        val diceSetToAdd = SetEntity(name = "a_name", diceSideColorArgb = 0, diceNumberColorArgb = 0)
        val diceSetAfterAdding = diceSetToAdd.copy(id = 1)
        val updatedSet = diceSetAfterAdding.copy(name = "f_name", diceSideColorArgb = 32, diceNumberColorArgb = 99)

        setDao.add(diceSetToAdd)
        setDao.update(updatedSet)

        val retrievedSets = setDao.retrieveAll().take(1).single()
        assertEquals(1, retrievedSets.size)
        assertEquals(updatedSet, retrievedSets[0])
    }

    @Test
    fun deleteSet_withItsDiceAndShortcuts() = runTest {
        val setup = createBasicDatabaseSetup()
        insertBasicDatabaseSetup(setup)

        val retrievedSetsBefore = setDao.retrieveAll().take(1).single()
        val retrievedDiceBefore = dieDao.retrieveAllWithShortcuts().take(1).single()
        val retrievedShortcutsBefore = shortcutDao.retrieveAll().take(1).single()

        assertEquals(1, retrievedSetsBefore.size)
        assertEquals(2, retrievedDiceBefore.size)
        assertEquals(3, retrievedShortcutsBefore.size)

        setDao.delete(setup.set)

        val retrievedSetsAfter = setDao.retrieveAll().take(1).single()
        val retrievedDiceAfter = dieDao.retrieveAllWithShortcuts().take(1).single()
        val retrievedShortcutsAfter = shortcutDao.retrieveAll().take(1).single()

        assertEquals(0, retrievedSetsAfter.size)
        assertEquals(0, retrievedDiceAfter.size)
        assertEquals(0, retrievedShortcutsAfter.size)
    }

    @Test
    fun retrieveSet_withChosenId() = runTest {
        val setup = createBasicDatabaseSetup()
        insertBasicDatabaseSetup(setup)
        val retrievedSet = setDao.retrieveSetWithId(setup.set.id).take(1).single()

        retrievedSet?.let { set ->
            assertEquals("first_set", set.set.name)
            assertEquals(2, set.diceWithShortcuts.size)
            assertEquals(3, set.diceWithShortcuts.flatMap { it.shortcuts }.size)
        } ?: fail()

    }


}