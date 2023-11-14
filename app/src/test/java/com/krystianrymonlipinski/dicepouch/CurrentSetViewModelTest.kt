package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.graphics.Color
import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.model.ChosenSetScreenState
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.capture
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CurrentSetViewModelTest {

    private lateinit var testObj: CurrentSetViewModel

    @Mock
    lateinit var setsLocalDataSource: SetsLocalDataSource
    @Mock
    lateinit var diceLocalDataSourceImpl: DiceLocalDataSourceImpl
    @Mock
    lateinit var shortcutsLocalDataSourceImpl: ShortcutsLocalDataSourceImpl
    @Mock
    lateinit var settingsLocalDataSourceImpl: SettingsLocalDataSourceImpl

    @Captor
    lateinit var dieCaptor: ArgumentCaptor<Die>
    @Captor
    lateinit var shortcutCaptor: ArgumentCaptor<RollShortcut>


    @Before
    fun setUp() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        testObj = CurrentSetViewModel(setsLocalDataSource, diceLocalDataSourceImpl,
            shortcutsLocalDataSourceImpl, settingsLocalDataSourceImpl)
    }

    @Test
    fun detectCurrentSetStreamChange() = runTest {
        val die = Die(sides = 20)
        val setToEmit = DiceSet(
            info = DiceSetInfo(id = 10, name = "a_name"),
            dice = listOf(die),
            shortcuts = listOf(RollShortcut(name = "sh", setting = RollSetting(die = die)))
        )
        whenever(setsLocalDataSource.retrieveSetWithId(anyInt())).thenReturn(flowOf(setToEmit))

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testObj.chosenSetScreenState.collect { /* Collector (even an empty one) needed for .stateIn operator to work properly */ }
        }

        assertEquals(null, testObj.chosenSetScreenState.value.chosenSet)
        testObj.setCurrentSet(1)
        assertEquals(setToEmit, testObj.chosenSetScreenState.value.chosenSet)
    }

    @Test
    fun addNewDie() = runTest {
        val dieToBeAdded = Die(sides = 6, sideColor = Color.White, numberColor = Color.Black)
        testObj.chosenSetScreenState.value = ChosenSetScreenState(true, DiceSet())

        testObj.addNewDieToSet(numberOfSides = 6)
        verify(diceLocalDataSourceImpl).addNewDieToSet(anyInt(), capture(dieCaptor))
        assertEqualsDie_withoutTimestampId(dieToBeAdded, dieCaptor.value)
    }

    @Test
    fun deleteDie() = runTest {
        val dieToBeDeleted = Die(sides = 8, sideColor = Color.White, numberColor = Color.Black)
        testObj.chosenSetScreenState.value = ChosenSetScreenState(true, DiceSet())

        testObj.deleteDieFromSet(die = dieToBeDeleted)
        verify(diceLocalDataSourceImpl).deleteDieFromSet(anyInt(), capture(dieCaptor))
        assertEqualsDie_withoutTimestampId(dieToBeDeleted, dieCaptor.value)
    }

    @Test
    fun addNewShortcut() = runTest {
        val shortcutToBeAdded = RollShortcut(
            timestampId = 1L,
            name = "random_name",
            setting = RollSetting(
                die = Die(sides = 5),
                modifier = -2
            )
        )
        testObj.addNewShortcutToSet(
            name = "random_name",
            setting = RollSetting(die = Die(sides = 5), modifier = -2)
        )

        verify(shortcutsLocalDataSourceImpl).addNewShortcutToSet(capture(shortcutCaptor))
        assertEqualsRollShortcut_withoutTimestampId(shortcutToBeAdded, shortcutCaptor.value)
    }

    @Test
    fun updateShortcut() = runTest {
        val updatedShortcut = RollShortcut(
            timestampId = 1L,
            name = "random_name",
            setting = RollSetting(
                die = Die(sides = 3),
                mechanic = RollSetting.Mechanic.ADVANTAGE
            )
        )
        testObj.updateShortcut(RollShortcut(
            name = "random_name",
            setting = RollSetting(die = Die(sides = 3), mechanic = RollSetting.Mechanic.ADVANTAGE)
        ))

        verify(shortcutsLocalDataSourceImpl).updateShortcut(capture(shortcutCaptor))
        assertEqualsRollShortcut_withoutTimestampId(updatedShortcut, shortcutCaptor.value)
    }

    @Test
    fun deleteShortcut() = runTest {
        val shortcutToBeDeleted = RollShortcut(
            timestampId = 1L,
            name = "random_name",
            setting = RollSetting(
                die = Die(sides = 3),
                mechanic = RollSetting.Mechanic.ADVANTAGE
            )
        )
        testObj.deleteShortcut(shortcutToBeDeleted)

        verify(shortcutsLocalDataSourceImpl).deleteShortcutFromSet(capture(shortcutCaptor))
        assertEqualsRollShortcut_withoutTimestampId(shortcutToBeDeleted, shortcutCaptor.value)
    }

    private fun assertEqualsDie_withoutTimestampId(assumedDie: Die, capturedDie: Die) {
        assertEquals(assumedDie.sides, capturedDie.sides)
        assertEquals(assumedDie.sideColor, capturedDie.sideColor)
        assertEquals(assumedDie.numberColor, capturedDie.numberColor)
    }

    private fun assertEqualsShortcutSetting(assumedSetting: RollSetting, capturedSetting: RollSetting) {
        assertEqualsDie_withoutTimestampId(assumedSetting.die, capturedSetting.die)
        assertEquals(assumedSetting.diceNumber, capturedSetting.diceNumber)
        assertEquals(assumedSetting.modifier, capturedSetting.modifier)
        assertEquals(assumedSetting.mechanic, capturedSetting.mechanic)
    }

    private fun assertEqualsRollShortcut_withoutTimestampId(assumedShortcut: RollShortcut, capturedShortcut: RollShortcut) {
        assertEquals(assumedShortcut.name, capturedShortcut.name)
        assertEqualsShortcutSetting(assumedShortcut.setting, capturedShortcut.setting)
    }

}