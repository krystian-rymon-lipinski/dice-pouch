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
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.kotlin.capture
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class DiceLocalDataSourceImplTest {

    private lateinit var testObj: DiceLocalDataSourceImpl

    @Mock
    private lateinit var dieDao: DieDao

    @Captor
    private lateinit var dieEntityCaptor: ArgumentCaptor<DieEntity>

    @Before
    fun setUp() {
        testObj = DiceLocalDataSourceImpl(dieDao)
    }


    @Test
    fun addNewDieToSet() = runTest {
        val setId = 1
        val dieToAdd = Die(sides = 6, timestampId = 10, sideColor = Color.Magenta, numberColor = Color.Blue)
        val dieEntityToCheck = DieEntity(timestampId = 10, setId = 1, sides = 6, sidesColorArgb = Color.Magenta.toArgb(), numberColorArgb = Color.Blue.toArgb())

        testObj.addNewDieToSet(setId, dieToAdd)
        verify(dieDao).add(capture(dieEntityCaptor))
        assertEquals(dieEntityToCheck, dieEntityCaptor.value)
    }

    @Test
    fun deleteDieFromSet() = runTest {
        val setId = 1
        val dieToDelete = Die(sides = 6, timestampId = 10, sideColor = Color.Magenta, numberColor = Color.Blue)
        val dieEntityToCheck = DieEntity(timestampId = 10, setId = 1, sides = 6, sidesColorArgb = Color.Magenta.toArgb(), numberColorArgb = Color.Blue.toArgb())

        testObj.deleteDieFromSet(setId, dieToDelete)
        verify(dieDao).delete(capture(dieEntityCaptor))
        assertEquals(dieEntityToCheck, dieEntityCaptor.value)
    }
}