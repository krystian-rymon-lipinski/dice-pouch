package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.graphics.Color
import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    private lateinit var testObj: MainActivityViewModel

    @Mock
    lateinit var setsLocalDataSource: SetsLocalDataSource
    @Mock
    lateinit var diceLocalDataSourceImpl: DiceLocalDataSourceImpl
    @Mock
    lateinit var shortcutsLocalDataSourceImpl: ShortcutsLocalDataSourceImpl

    @Captor
    lateinit var setCaptor: ArgumentCaptor<DiceSetInfo>
    @Captor
    lateinit var dieCaptor: ArgumentCaptor<Die>
    @Captor
    lateinit var shortcutCaptor: ArgumentCaptor<RollShortcut>

    @Before
    fun setUp() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        testObj = MainActivityViewModel(setsLocalDataSource, diceLocalDataSourceImpl, shortcutsLocalDataSourceImpl)
    }

    @Test
    fun detectCurrentSetStreamChange() = runTest {
        val die = Die(sides = 20)
        val setToEmit = DiceSet(info = DiceSetInfo(id = 10, name = "a_name"), dice = listOf(die),
                shortcuts = listOf(RollShortcut(name = "sh", setting = RollSetting(die = die))))
        val setsSource = FakeSetsLocalDataSource()
        val testObj2 = MainActivityViewModel(setsSource, diceLocalDataSourceImpl, shortcutsLocalDataSourceImpl)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testObj2.diceSetState.collect { /* Collector (even an empty one) needed for .stateIn operator to work properly */ }
        }

        assertEquals(DiceSet(), testObj2.diceSetState.value)
        setsSource.emitSet(setToEmit)
        assertEquals(setToEmit, testObj2.diceSetState.value)
    }

    @Test
    fun detectSetsStreamChange() = runTest {
        val setsToEmit = listOf(
            DiceSetInfo(2, "grr", Color.White, Color.Black),
            DiceSetInfo(3, "wrrr", Color.Black, Color.White)
        )
        val setsSource = FakeSetsLocalDataSource()
        val testObj2 = MainActivityViewModel(setsSource, diceLocalDataSourceImpl, shortcutsLocalDataSourceImpl)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testObj2.allSetsState.collect { /* Collector (even an empty one) needed for .stateIn operator to work properly */ }
        }

        assertEquals(emptyList<DiceSetInfo>(), testObj2.allSetsState.value)
        setsSource.emitSets(setsToEmit)
        assertEquals(setsToEmit, testObj2.allSetsState.value)
    }


    @Test
    fun addNewSet() = runTest {
        val setToBeAdded = DiceSetInfo(name = "a_name", diceColor = Color.White, numbersColor = Color.Black)

        testObj.addNewSet(name = "a_name", diceColor = Color.White, numbersColor = Color.Black)
        verify(setsLocalDataSource).addDiceSet(capture(setCaptor))
        assertEquals(setToBeAdded, setCaptor.value)
    }

    @Test
    fun deleteSet() = runTest {
        val setToBeDeleted = DiceSetInfo(name = "z_name", diceColor = Color.White, numbersColor = Color.Cyan)

        testObj.deleteSet(setToBeDeleted)
        verify(setsLocalDataSource).deleteDiceSet(capture(setCaptor))
        assertEquals(setToBeDeleted, setCaptor.value)
    }

    @Test
    fun addNewDie() = runTest {
        val dieToBeAdded = Die(sides = 6, sideColor = Color.White, numberColor = Color.Black)

        testObj.addNewDieToSet(numberOfSides = 6)
        verify(diceLocalDataSourceImpl).addNewDieToSet(anyInt(), capture(dieCaptor))
        assertEqualsDie_withoutTimestampId(dieToBeAdded, dieCaptor.value)
    }

    @Test
    fun deleteDie() = runTest {
        val dieToBeDeleted = Die(sides = 8, sideColor = Color.White, numberColor = Color.Black)

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

    private class FakeSetsLocalDataSource : SetsLocalDataSource {
        private val allSetsFlow = MutableStateFlow(emptyList<DiceSetInfo>())
        private val currentSetFlow = MutableStateFlow(DiceSet())

        suspend fun emitSets(value: List<DiceSetInfo>) { allSetsFlow.emit(value) }
        suspend fun emitSet(value: DiceSet) { currentSetFlow.emit(value) }
        override fun retrieveAllSetsInfo() = allSetsFlow
        override fun retrieveSetWithName(name: String) = currentSetFlow

        override suspend fun addDiceSet(set: DiceSetInfo) { /* Do nothing */ }
        override suspend fun deleteDiceSet(set: DiceSetInfo) { /* Do nothing */ }

    }

}