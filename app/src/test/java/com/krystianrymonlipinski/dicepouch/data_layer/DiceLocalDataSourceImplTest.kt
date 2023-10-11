package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.DieDao
import com.krystianrymonlipinski.dicepouch.room.DieEntity
import com.krystianrymonlipinski.dicepouch.room.DieWithShortcuts
import com.krystianrymonlipinski.dicepouch.room.ShortcutEntity
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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
    fun getDiceStream() = runTest {
        val dieEntity1 = DieEntity(timestampId = 4, setId = 1, sides = 4, sidesColorArgb = Color.White.toArgb(), numberColorArgb = Color.Black.toArgb())
        val dieEntity2 = DieEntity(timestampId = 12, setId = 1, sides = 8, sidesColorArgb = Color.Red.toArgb(), numberColorArgb = Color.Green.toArgb())
        val dieToCheck1 = Die(timestampId = 4, sides = 4, sideColor = Color.White, numberColor = Color.Black)
        val dieToCheck2 = Die(timestampId = 12, sides = 8, sideColor = Color.Red, numberColor = Color.Green)

        val shortcutsList = mock<List<ShortcutEntity>>()
        whenever(dieDao.retrieveAllWithShortcuts()).thenReturn(flowOf(listOf(
            DieWithShortcuts(dieEntity1, shortcutsList),
            DieWithShortcuts(dieEntity2, shortcutsList)
        )))
        val testObj2 = DiceLocalDataSourceImpl(dieDao)

        val diceRetrieved = testObj2.getDiceStream().take(1).single()
        assertEquals(2, diceRetrieved.size)
        assertEquals(dieToCheck1, diceRetrieved[0])
        assertEquals(dieToCheck2, diceRetrieved[1])
    }

    @Test
    fun getShortcutsStream_transformFromTwoRelatedDatabaseObjects() = runTest {
        val dieWithShortcuts = DieWithShortcuts(
            die = DieEntity(
                timestampId = 10L,
                setId = 1,
                sides = 4,
                sidesColorArgb = Color.White.toArgb(),
                numberColorArgb = Color.Black.toArgb()
            ),
            shortcuts = listOf(ShortcutEntity(
                timestampId = 1L,
                name = "a_name",
                diceNumber = 3,
                dieId = 10L,
                modifier = -2,
                mechanic = "DISADVANTAGE"
            ))
        )

        val shortcutsToRetrieve = listOf(RollShortcut(
            timestampId = 1L,
            name = "a_name",
            setting = RollSetting(
                die = Die(
                    timestampId = 10L,
                    sides = 4,
                    sideColor = Color.White,
                    numberColor = Color.Black
                ),
                diceNumber = 3,
                modifier = -2,
                mechanic = RollSetting.Mechanic.DISADVANTAGE
            )
        ))

        whenever(dieDao.retrieveAllWithShortcuts()).thenReturn(flowOf(listOf(dieWithShortcuts)))
        val testObj2 = DiceLocalDataSourceImpl(dieDao)

        val retrievedShortcuts = testObj2.getShortcutsStream().take(1).single()
        assertEquals(1, retrievedShortcuts.size)
        assertEquals(shortcutsToRetrieve, retrievedShortcuts)
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