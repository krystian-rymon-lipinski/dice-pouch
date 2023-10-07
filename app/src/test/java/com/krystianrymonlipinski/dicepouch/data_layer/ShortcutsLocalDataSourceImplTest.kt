package com.krystianrymonlipinski.dicepouch.data_layer

import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.ShortcutDao
import com.krystianrymonlipinski.dicepouch.room.ShortcutEntity
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

@RunWith(MockitoJUnitRunner::class)
class ShortcutsLocalDataSourceImplTest {

    private lateinit var testObj: ShortcutsLocalDataSourceImpl

    @Mock
    lateinit var shortcutDao: ShortcutDao

    @Captor
    lateinit var shortcutEntityCaptor: ArgumentCaptor<ShortcutEntity>


    @Before
    fun setUp() {
        testObj = ShortcutsLocalDataSourceImpl(shortcutDao)
    }

    @Test
    fun addNewShortcut() = runTest {
        val shortcutToAdd = RollShortcut(
            timestampId = 0L,
            name = "a_name",
            setting = RollSetting(Die(sides = 6, timestampId = 1L))
        )
        val shortcutEntityToCheck = ShortcutEntity(
            timestampId = 0L,
            name = "a_name",
            diceNumber = 1,
            dieId = 1L,
            modifier = 0,
            mechanic = RollSetting.Mechanic.NORMAL.toString()
        )

        testObj.addNewShortcutToSet(shortcutToAdd)
        verify(shortcutDao).add(capture(shortcutEntityCaptor))
        assertEquals(shortcutEntityToCheck, shortcutEntityCaptor.value)
    }

    @Test
    fun deleteShortcut() = runTest {
        val shortcutToDelete = RollShortcut(
            timestampId = 12L,
            name = "c_name",
            setting = RollSetting(Die(sides = 12, timestampId = 25L), mechanic = RollSetting.Mechanic.DISADVANTAGE)
        )
        val shortcutEntityToCheck = ShortcutEntity(
            timestampId = 12L,
            name = "c_name",
            diceNumber = 1,
            dieId = 25L,
            modifier = 0,
            mechanic = RollSetting.Mechanic.DISADVANTAGE.toString()
        )

        testObj.deleteShortcutFromSet(shortcutToDelete)
        verify(shortcutDao).delete(capture(shortcutEntityCaptor))
        assertEquals(shortcutEntityToCheck, shortcutEntityCaptor.value)
    }

    @Test
    fun updateShortcut() = runTest {
        val shortcutToUpdate = RollShortcut(
            timestampId = 102L,
            name = "f_name",
            setting = RollSetting(Die(sides = 4, timestampId = 6L), modifier = -5, diceNumber = 12)
        )
        val shortcutEntityToCheck = ShortcutEntity(
            timestampId = 102L,
            name = "f_name",
            diceNumber = 12,
            dieId = 6L,
            modifier = -5,
            mechanic = RollSetting.Mechanic.NORMAL.toString()
        )

        testObj.updateShortcut(shortcutToUpdate)
        verify(shortcutDao).update(capture(shortcutEntityCaptor))
        assertEquals(shortcutEntityToCheck, shortcutEntityCaptor.value)
    }

}