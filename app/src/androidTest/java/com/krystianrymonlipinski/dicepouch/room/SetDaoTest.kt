package com.krystianrymonlipinski.dicepouch.room

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetDaoTest : BaseDaoTest() {


    @Test
    fun addDiceSet() = runTest {
        val diceSetToAdd1 = SetEntity(name = "a_name", diceSideColorArgb = 0, diceNumberColorArgb = 0)
        val diceSetToAdd2 = SetEntity(name = "b_name", diceSideColorArgb = 12, diceNumberColorArgb = 17)
        val diceSetAfterAdding1 = diceSetToAdd1.copy(id = 1) // check for auto increment
        val diceSetAfterAdding2 = diceSetToAdd2.copy(id = 2)

        setDao.add(diceSetToAdd1)
        setDao.add(diceSetToAdd2)

        val retrievedSets = setDao.retrieveAllWithDice().take(1).single()
        assertEquals(2, retrievedSets.size)
        assertEquals(diceSetAfterAdding1, retrievedSets[0].set)
        assertEquals(diceSetAfterAdding2, retrievedSets[1].set)
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

        val retrievedSets = setDao.retrieveAllWithDice().take(1).single()
        assertEquals(1, retrievedSets.size)
        assertEquals(diceSetToSpare, retrievedSets[0].set)
    }

    @Test
    fun updateDiceSet() = runTest {
        val diceSetToAdd = SetEntity(name = "a_name", diceSideColorArgb = 0, diceNumberColorArgb = 0)
        val setUpdate = diceSetToAdd.copy(name = "f_name", diceNumberColorArgb = 99)
        val diceSetAfterAdding = diceSetToAdd.copy(id = 1)

        setDao.add(diceSetToAdd)
        setDao.update(setUpdate)

        val retrievedSets = setDao.retrieveAllWithDice().take(1).single()
        assertEquals(1, retrievedSets.size)
        assertEquals(diceSetAfterAdding, retrievedSets[0].set)
    }

    @Test
    fun deleteSet_withItsDiceAndShortcuts() = runTest {
        val setup = createBasicDatabaseSetup()
        setDao.delete(setup.set)

        val retrievedSets = setDao.retrieveAllWithDice().take(1).single()
        val retrievedDice = dieDao.retrieveAllWithShortcuts().take(1).single()
        val retrievedShortcuts = shortcutDao.retrieveAll().take(1).single()

        assertEquals(0, retrievedSets.size)
        assertEquals(0, retrievedDice.size)
        assertEquals(0, retrievedShortcuts.size)
    }


}