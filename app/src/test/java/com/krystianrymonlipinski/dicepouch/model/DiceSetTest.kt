package com.krystianrymonlipinski.dicepouch.model

import androidx.compose.ui.graphics.Color
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DiceSetTest {

    private lateinit var testObj: DiceSet


    @Before
    fun setUp() {
        testObj = DiceSet()
    }

    @Test
    fun changeInfo() {
        val newInfo = DiceSetInfo(name = "b_name", numbersColor = Color.LightGray)
        testObj = testObj.changeInfo(newInfo = newInfo)
        assertEquals(newInfo, testObj.info)
    }

    @Test
    fun addNewDie() {
        val dieToAdd = Die(8)
        testObj = testObj.addNewDie(dieToAdd)

        assertEquals(1, testObj.dice.size)
        assertEquals(dieToAdd, testObj.dice[0])
    }

    @Test
    fun deleteDie() {
        val dieToRemain = Die(20)
        testObj = testObj
            .addNewDie(Die(10))
            .addNewDie(dieToRemain)
        testObj = testObj.deleteDie(0)

        assertEquals(1, testObj.dice.size)
        assertEquals(dieToRemain, testObj.dice[0])
    }

    @Test
    fun addShortcut() {
        val newShortcut = RollShortcut(0L, "a_shortcut", RollSetting(Die(8)))
        testObj = testObj.addNewShortcut(newShortcut)

        assertEquals(1, testObj.shortcuts.size)
        assertEquals(newShortcut, testObj.shortcuts[0])
    }

    @Test
    fun changeShortcut() {
        val changedShortcut = RollShortcut(10L, "new_name", RollSetting(Die(20), 2, -1, RollSetting.Mechanic.ADVANTAGE))
        testObj = testObj.copy(shortcuts = listOf(
            RollShortcut(0L, "a_shortcut", RollSetting(Die(8))),
            RollShortcut(1L, "b_shortcut", RollSetting(Die(20)))
        ))

        testObj = testObj.changeShortcut(0, changedShortcut)
        assertEquals(changedShortcut, testObj.shortcuts[0])
    }

    @Test
    fun deleteShortcut() {
        val shortcutToRemain = RollShortcut(0L, "name", RollSetting(Die(12)))
        testObj = testObj.copy(shortcuts = listOf(
            RollShortcut(0L, "a_shortcut", RollSetting(Die(8))),
            shortcutToRemain
        ))

        testObj = testObj.deleteShortcut(0)
        assertEquals(1, testObj.shortcuts.size)
        assertEquals(shortcutToRemain, testObj.shortcuts[0])
    }
}