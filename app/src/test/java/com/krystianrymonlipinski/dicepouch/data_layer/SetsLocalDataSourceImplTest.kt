package com.krystianrymonlipinski.dicepouch.data_layer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.DieEntity
import com.krystianrymonlipinski.dicepouch.room.DieWithShortcuts
import com.krystianrymonlipinski.dicepouch.room.SetDao
import com.krystianrymonlipinski.dicepouch.room.SetEntity
import com.krystianrymonlipinski.dicepouch.room.SetWithDice
import com.krystianrymonlipinski.dicepouch.room.ShortcutEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
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

    @Test
    fun retrieveSet_withName() = runTest {
        val setWithDice = SetWithDice(
            set = SetEntity(id = 4, name = "g_name", diceSideColorArgb = 23, diceNumberColorArgb = 10),
            diceWithShortcuts = listOf(DieWithShortcuts(
                die = DieEntity(timestampId = 1L, setId = 4, sides = 5, sidesColorArgb = 2, numberColorArgb = 99),
                shortcuts = listOf(ShortcutEntity(timestampId = 100L, name = "short", diceNumber = 12,
                    dieId = 1L, modifier = -2, mechanic = "NORMAL"))
            ))
        )
        val setToBeRetrieved = DiceSet(
            id = 4,
            name = "g_name",
            dice = listOf(Die(timestampId = 1L, sides = 5, sideColor = Color(2), numberColor = Color(99))),
            shortcuts = listOf(RollShortcut(timestampId = 100L, name = "short",
                setting = RollSetting(
                    die = Die(timestampId = 1L, sides = 5, sideColor = Color(2), numberColor = Color(99)),
                    diceNumber = 12,
                    modifier = -2,
                    mechanic = RollSetting.Mechanic.NORMAL
                )))
        )

        whenever(setDao.retrieveSetWithName(anyString())).thenReturn(flowOf(setWithDice))

        val retrievedSet = testObj.retrieveSetWithName("set_name").take(1).single()
        verify(setDao).retrieveSetWithName("set_name")
        assertEquals(setToBeRetrieved, retrievedSet)
    }
}