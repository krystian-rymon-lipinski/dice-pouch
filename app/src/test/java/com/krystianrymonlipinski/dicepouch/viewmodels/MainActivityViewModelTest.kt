package com.krystianrymonlipinski.dicepouch.viewmodels

import androidx.compose.ui.graphics.Color
import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSource
import com.krystianrymonlipinski.dicepouch.model.ChosenSetScreenState
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.PouchScreenState
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.capture
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    private lateinit var testObj: MainActivityViewModel

    @Mock
    lateinit var setsLocalDataSourceImpl: SetsLocalDataSource
    @Mock
    lateinit var diceLocalDataSourceImpl: DiceLocalDataSource
    @Mock
    lateinit var shortcutsLocalDataSourceImpl: ShortcutsLocalDataSource
    @Mock
    lateinit var settingsLocalDataSourceImpl: SettingsLocalDataSource

    @Captor
    lateinit var dieCaptor: ArgumentCaptor<Die>
    @Captor
    lateinit var shortcutCaptor: ArgumentCaptor<RollShortcut>
    @Captor
    lateinit var setCaptor: ArgumentCaptor<DiceSetInfo>
    @Captor
    lateinit var savedSettingsCaptor: ArgumentCaptor<RollingSettings>


    @Before
    fun setUp() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        testObj = MainActivityViewModel(diceLocalDataSourceImpl, shortcutsLocalDataSourceImpl,
            setsLocalDataSourceImpl, settingsLocalDataSourceImpl)
    }



    @Test
    fun detectCurrentSetScreenStateChange() = runTest {

        val die = Die(sides = 20)
        val setToEmit = DiceSet(
            info = DiceSetInfo(id = 10, name = "a_name"),
            dice = listOf(die),
            shortcuts = listOf(RollShortcut(name = "sh", setting = RollSetting(die = die)))
        )
        val setsSource = FakeSetsLocalDataSource()
        val settingsSource = FakeSettingsLocalDataSource()
        val testObj2 = MainActivityViewModel(diceLocalDataSourceImpl, shortcutsLocalDataSourceImpl,
            setsSource, settingsSource)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testObj2.tableScreenState.collect { /* Collector (even an empty one) needed for .stateIn operator to work properly */ }
        }

        assertEquals(ChosenSetScreenState(false, null), testObj2.tableScreenState.value)
        testObj2.currentSetState.value = setToEmit
        testObj2.isLoadingFinishedState.value = true
        assertEquals(ChosenSetScreenState(true, setToEmit), testObj2.tableScreenState.value)

/*
        val die = Die(sides = 20)
        val setToEmit = DiceSet(
            info = DiceSetInfo(id = 10, name = "a_name"),
            dice = listOf(die),
            shortcuts = listOf(RollShortcut(name = "sh", setting = RollSetting(die = die)))
        )
        whenever(settingsLocalDataSourceImpl.retrieveCurrentSetId()).thenReturn(flowOf(10))
        whenever(setsLocalDataSourceImpl.retrieveSetWithId(anyInt())).thenReturn(flowOf(setToEmit))

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testObj.tableScreenState.collect { /* Collector (even an empty one) needed for .stateIn operator to work properly */ }
        }

        assertEquals(ChosenSetScreenState(false, null), testObj.tableScreenState.value)
        testObj.initiateCurrentSet()
        assertEquals(ChosenSetScreenState(true, setToEmit), testObj.tableScreenState.value)

 */
    }

    @Test
    fun detectPouchScreenStateChange() = runTest {
        val setsToEmit = listOf(
            DiceSetInfo(2, "grr", Color.White, Color.Black),
            DiceSetInfo(3, "wrrr", Color.Black, Color.White)
        )
        val setsSource = FakeSetsLocalDataSource()
        val settingsSource = FakeSettingsLocalDataSource()
        val testObj2 = MainActivityViewModel(diceLocalDataSourceImpl, shortcutsLocalDataSourceImpl,
            setsSource, settingsSource)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testObj2.pouchScreenState.collect { /* Collector (even an empty one) needed for .stateIn operator to work properly */ }
        }

        assertEquals(PouchScreenState(emptyList(), null), testObj2.pouchScreenState.value)
        setsSource.emitSets(setsToEmit)
        assertEquals(PouchScreenState(setsToEmit, null), testObj2.pouchScreenState.value)
        testObj2.currentSetIdState.value = 3
        assertEquals(PouchScreenState(setsToEmit, 3), testObj2.pouchScreenState.value)
    }


    @Test
    fun addNewDie() = runTest {
        val dieToBeAdded = Die(sides = 6, sideColor = Color.White, numberColor = Color.Black)

        testObj.addNewDieToSet(chosenSetId = 2, numberOfSides = 6)
        verify(diceLocalDataSourceImpl).addNewDieToSet(eq(2), capture(dieCaptor))
        assertEqualsDie_withoutTimestampId(dieToBeAdded, dieCaptor.value)
    }

    @Test
    fun deleteDie() = runTest {
        val dieToBeDeleted = Die(sides = 8, sideColor = Color.White, numberColor = Color.Black)

        testObj.deleteDieFromSet(chosenSetId = 2, die = dieToBeDeleted)
        verify(diceLocalDataSourceImpl).deleteDieFromSet(eq(2), capture(dieCaptor))
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
        testObj.updateShortcut(
            RollShortcut(
            name = "random_name",
            setting = RollSetting(die = Die(sides = 3), mechanic = RollSetting.Mechanic.ADVANTAGE)
        )
        )

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

    @Test
    fun addNewSet() = runTest {
        val setToBeAdded = DiceSetInfo(name = "a_name", diceColor = Color.White, numbersColor = Color.Black)

        testObj.addNewSet(name = "a_name", diceColor = Color.White, numbersColor = Color.Black)
        verify(setsLocalDataSourceImpl).addDiceSet(capture(setCaptor))
        assertEquals(setToBeAdded, setCaptor.value)
    }

    @Test
    fun deleteSet() = runTest {
        val setToBeDeleted = DiceSetInfo(name = "z_name", diceColor = Color.White, numbersColor = Color.Cyan)

        testObj.deleteSet(setToBeDeleted)
        verify(setsLocalDataSourceImpl).deleteDiceSet(capture(setCaptor))
        assertEquals(setToBeDeleted, setCaptor.value)
    }

    @Test
    fun saveSettings() = runTest {
        val settingsToSave = RollingSettings()

        testObj.saveSettings(settingsToSave)
        verify(settingsLocalDataSourceImpl).saveRollingSettings(capture(savedSettingsCaptor))
        assertEquals(settingsToSave, savedSettingsCaptor.value)
    }

    @Test
    fun retrieveSettings() = runTest {
        val settingsToRetrieve = RollingSettings()
            .setIsRollPopupAutodismissOn(true)
            .setSingleThrowTimeMillis(2400)
        whenever(settingsLocalDataSourceImpl.retrieveRollingSettings()).thenReturn(flowOf(settingsToRetrieve))

        val settingsRetrieved = testObj.retrieveSettings()
        assertEquals(settingsToRetrieve, settingsRetrieved)
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
        private val setWithIdStream: MutableStateFlow<DiceSet?> = MutableStateFlow(null)

        suspend fun emitSets(value: List<DiceSetInfo>) { allSetsFlow.emit(value) }
        suspend fun emitSetWithId(id: Int, set: DiceSet) { setWithIdStream.emit(set) }
        override fun retrieveAllSetsInfo() = allSetsFlow
        override fun retrieveSetWithId(id: Int) = setWithIdStream

        override suspend fun addDiceSet(set: DiceSetInfo) { /* Do nothing */ }
        override suspend fun deleteDiceSet(set: DiceSetInfo) { /* Do nothing */ }
    }

    private class FakeSettingsLocalDataSource : SettingsLocalDataSource {
        private val setIdFlow: MutableStateFlow<Int?> = MutableStateFlow(null)

        suspend fun emitSetId(value: Int) { setIdFlow.emit(value) }
        override fun retrieveCurrentSetId(): Flow<Int?> = setIdFlow

        override suspend fun changeCurrentSet(chosenId: Int) { /* Do nothing */ }
        override fun retrieveRollingSettings(): Flow<RollingSettings> = flowOf(RollingSettings())
        override suspend fun saveRollingSettings(rollingSettings: RollingSettings) { /* Do nothing */ }
    }


}