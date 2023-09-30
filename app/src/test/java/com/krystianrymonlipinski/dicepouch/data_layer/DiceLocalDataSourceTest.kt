package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.room.DieDao
import com.krystianrymonlipinski.dicepouch.room.DieEntity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.kotlin.capture
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class DiceLocalDataSourceTest {

    private lateinit var testObj: DiceLocalDataSource

    @Mock
    private lateinit var dieDao: DieDao

    @Captor
    private lateinit var dieEntityCaptor: ArgumentCaptor<DieEntity>

    @Before
    fun setUp() {
        testObj = DiceLocalDataSource(dieDao)
    }


    @Test
    fun getDiceStream() = runTest {
        val dieEntity1 = DieEntity(timestampId = 4, sides = 4, sidesColorArgb = Color.White.toArgb(), numberColorArgb = Color.Black.toArgb())
        val dieEntity2 = DieEntity(timestampId = 12, sides = 8, sidesColorArgb = Color.Red.toArgb(), numberColorArgb = Color.Green.toArgb())
        val dieToCheck1 = Die(timestampId = 4, sides = 4, sideColor = Color.White, numberColor = Color.Black)
        val dieToCheck2 = Die(timestampId = 12, sides = 8, sideColor = Color.Red, numberColor = Color.Green)

        whenever(dieDao.retrieveAll()).thenReturn(flowOf(listOf(dieEntity1, dieEntity2)))
        val diceRetrieved = testObj.getDiceStream().take(1).single()

        assertEquals(2, diceRetrieved.size)
        assertEquals(dieToCheck1, diceRetrieved[0])
        assertEquals(dieToCheck2, diceRetrieved[1])
    }

    @Test
    fun addNewDieToSet() = runTest {
        val dieToAdd = Die(sides = 6, timestampId = 10, sideColor = Color.Magenta, numberColor = Color.Blue)
        val dieEntityToCheck = DieEntity(timestampId = 10, sides = 6, sidesColorArgb = Color.Magenta.toArgb(), numberColorArgb = Color.Blue.toArgb())

        testObj.addNewDieToSet(dieToAdd)
        verify(dieDao).add(capture(dieEntityCaptor))
        assertEquals(dieEntityToCheck, dieEntityCaptor.value)
    }

    @Test
    fun deleteDieFromSet() = runTest {
        val dieToDelete = Die(sides = 6, timestampId = 10, sideColor = Color.Magenta, numberColor = Color.Blue)
        val dieEntityToCheck = DieEntity(timestampId = 10, sides = 6, sidesColorArgb = Color.Magenta.toArgb(), numberColorArgb = Color.Blue.toArgb())

        testObj.deleteDieFromSet(dieToDelete)
        verify(dieDao).delete(capture(dieEntityCaptor))
        assertEquals(dieEntityToCheck, dieEntityCaptor.value)
    }
}