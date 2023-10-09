package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.room.SetDao
import com.krystianrymonlipinski.dicepouch.room.SetEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.capture
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class SetsLocalDataSourceImplTest {

    private lateinit var testObj: SetsLocalDataSource

    @Mock
    lateinit var setDao: SetDao

    @Captor
    lateinit var setEntityCaptor: ArgumentCaptor<SetEntity>


    @Before
    fun setUp() {
        testObj = SetsLocalDataSourceImpl(setDao)
    }

    @Test
    fun addSet() = runTest {
        val setToAdd = DiceSetInfo(name = "a_name", diceColor = Color.Blue, numbersColor = Color.LightGray)
        val entityToBeAdded = SetEntity(name = "a_name", diceSideColorArgb = Color.Blue.toArgb(), diceNumberColorArgb = Color.LightGray.toArgb())

        testObj.addDiceSet(setToAdd)
        verify(setDao).add(capture(setEntityCaptor))
        assertEquals(entityToBeAdded, setEntityCaptor.value)
    }

    @Test
    fun deleteSet() = runTest {
        val setToBeDeleted = DiceSetInfo(name = "f_name", diceColor = Color.Blue, numbersColor = Color.LightGray)
        val entityToBeDeleted = SetEntity(name = "f_name", diceSideColorArgb = Color.Blue.toArgb(), diceNumberColorArgb = Color.LightGray.toArgb())

        testObj.deleteDiceSet(setToBeDeleted)
        verify(setDao).delete(capture(setEntityCaptor))
        assertEquals(entityToBeDeleted, setEntityCaptor.value)
    }

    @Test
    fun retrieveAllSets() = runTest {
        val setEntity1 = SetEntity(name = "b_name", diceSideColorArgb = 10, diceNumberColorArgb = 2)
        val setsInfoToBeRetrieved = DiceSetInfo(name = "b_name", diceColor = Color(10), numbersColor = Color(2))

        whenever(setDao.retrieveAll()).thenReturn(flowOf(listOf(setEntity1)))

        val retrievedSets = testObj.retrieveAllSetsInfo().take(1).single()
        verify(setDao).retrieveAll()
        assertEquals(setsInfoToBeRetrieved, retrievedSets[0])
    }
}