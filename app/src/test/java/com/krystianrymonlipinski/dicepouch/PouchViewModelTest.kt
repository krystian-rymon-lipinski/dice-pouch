package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.graphics.Color
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.viewmodels.PouchViewModel
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
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PouchViewModelTest {

    private lateinit var testObj: PouchViewModel

    @Mock
    lateinit var setsLocalDataSource: SetsLocalDataSource
    @Mock
    lateinit var settingsLocalDataSourceImpl: SettingsLocalDataSourceImpl

    @Captor
    lateinit var setCaptor: ArgumentCaptor<DiceSetInfo>


    @Before
    fun setUp() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        testObj = PouchViewModel(setsLocalDataSource, settingsLocalDataSourceImpl)
    }



    @Test
    fun detectSetsStreamChange() = runTest {
        val setsToEmit = listOf(
            DiceSetInfo(2, "grr", Color.White, Color.Black),
            DiceSetInfo(3, "wrrr", Color.Black, Color.White)
        )
        val setsSource = FakeSetsLocalDataSource()
        val testObj2 = PouchViewModel(setsSource, FakeSettingsLocalDataSource())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testObj2.allSetsState.collect { /* Collector (even an empty one) needed for .stateIn operator to work properly */ }
        }

        assertEquals(emptyList<DiceSetInfo>(), testObj2.allSetsState.value.allSets)
        setsSource.emitSets(setsToEmit)
        assertEquals(setsToEmit, testObj2.allSetsState.value.allSets)
    }

    @Test
    fun detectSetIdStreamChange() = runTest {
        val settingsSource = FakeSettingsLocalDataSource()
        val testObj2 = PouchViewModel(FakeSetsLocalDataSource(), settingsSource)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            testObj2.allSetsState.collect { }
        }

        assertEquals(null, testObj2.allSetsState.value.currentlyChosenSetId)
        settingsSource.emitSetId(20)
        assertEquals(20, testObj2.allSetsState.value.currentlyChosenSetId)
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



    private class FakeSetsLocalDataSource : SetsLocalDataSource {
        private val allSetsFlow = MutableStateFlow(emptyList<DiceSetInfo>())

        suspend fun emitSets(value: List<DiceSetInfo>) { allSetsFlow.emit(value) }
        override fun retrieveAllSetsInfo() = allSetsFlow

        override fun retrieveSetWithId(id: Int) = flowOf(DiceSet())
        override suspend fun addDiceSet(set: DiceSetInfo) { /* Do nothing */ }
        override suspend fun deleteDiceSet(set: DiceSetInfo) { /* Do nothing */ }
    }

    private class FakeSettingsLocalDataSource : SettingsLocalDataSource {
        private val setIdFlow: MutableStateFlow<Int?> = MutableStateFlow(null)

        suspend fun emitSetId(value: Int) { setIdFlow.emit(value) }
        override fun retrieveCurrentSetId(): Flow<Int?> = setIdFlow

        override suspend fun changeCurrentSet(chosenId: Int) { /* Do nothing */ }
    }

}