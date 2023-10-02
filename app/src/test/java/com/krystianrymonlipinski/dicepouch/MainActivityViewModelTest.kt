package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.graphics.Color
import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSourceImpl
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    private lateinit var testObj: MainActivityViewModel

    @Mock
    lateinit var diceLocalDataSourceImpl: DiceLocalDataSourceImpl
    @Mock
    lateinit var shortcutsLocalDataSourceImpl: ShortcutsLocalDataSourceImpl

    @Captor
    lateinit var dieCaptor: ArgumentCaptor<Die>
    @Captor
    lateinit var shortcutCaptor: ArgumentCaptor<RollShortcut>

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        whenever(diceLocalDataSourceImpl.getDiceStream()).thenReturn(flowOf(listOf()))
        whenever(shortcutsLocalDataSourceImpl.getShortcutsStream()).thenReturn(flowOf(listOf()))
        testObj = MainActivityViewModel(diceLocalDataSourceImpl, shortcutsLocalDataSourceImpl)

    }

    @Test
    fun addNewDie() = runTest {
        val dieToBeAdded = Die(sides = 6, sideColor = Color.White, numberColor = Color.Black)

        testObj.addNewDieToSet(numberOfSides = 6)
        verify(diceLocalDataSourceImpl).addNewDieToSet(capture(dieCaptor))
        assertEqualsDie_withoutTimestampId(dieToBeAdded, dieCaptor.value)
    }

    @Test
    fun deleteDie() = runTest {
        val dieToBeDeleted = Die(sides = 8, sideColor = Color.White, numberColor = Color.Black)
        testObj.deleteDieFromSet(die = dieToBeDeleted)

        verify(diceLocalDataSourceImpl).deleteDieFromSet(capture(dieCaptor))
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