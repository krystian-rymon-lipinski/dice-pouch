package com.krystianrymonlipinski.dicepouch.room

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetDaoTest : BaseDaoTest() {

    private lateinit var setDao: SetDao


    @Before
    fun setUpDao() {
        setDao = db.setDao()
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
        val setUpdate = diceSetToAdd.copy(name = "f_name", diceNumberColorArgb = 99)
        val diceSetAfterAdding = diceSetToAdd.copy(id = 1)

        setDao.add(diceSetToAdd)
        setDao.update(setUpdate)

        val retrievedSets = setDao.retrieveAll().take(1).single()
        assertEquals(1, retrievedSets.size)
        assertEquals(diceSetAfterAdding, retrievedSets[0])
    }


}