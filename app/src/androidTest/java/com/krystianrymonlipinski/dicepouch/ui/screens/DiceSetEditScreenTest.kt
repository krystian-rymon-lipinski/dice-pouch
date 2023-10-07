package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.BaseAndroidTest
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Ignore
import org.junit.Test

class DiceSetEditScreenTest : BaseAndroidTest() {


    @Test
    fun clickNewDie_showDialog() {
        composeTestRule.apply {
            setContent { DicePouchTheme { DiceSetEditScreen() } }

            onAllNodesWithContentDescription("add_icon")[0].performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("sides").assertIsDisplayed()
        }
    }

    @Test
    fun clickDeleteDie_whenDieHasShortcuts_shouldShowPopup_toCommunicateThatShortcutsWillBeDeletedToo() {
        val die = Die(20)
        composeTestRule.apply {
            setContent { DicePouchTheme { DiceSetEditScreen(DiceSet(
                dice = listOf(die),
                shortcuts = listOf(RollShortcut(name = "a_name", setting = RollSetting(die = die)))
            )) } }

            onAllNodesWithContentDescription("delete_die_icon")[0].performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText(text = "Do you wish to proceed?", substring = true)
        }
    }

    @Test
    fun clickDeleteDie_whenDieHasNoShortcuts_shouldNotShowPopup() {
        composeTestRule.apply {
            setContent { DicePouchTheme { DiceSetEditScreen(DiceSet(dice = listOf(Die(20)))) } }

            onAllNodesWithContentDescription("delete_die_icon")[0].performClick()
            onNode(isDialog()).assertDoesNotExist()
        }
    }

    @Test
    fun clickNewShortcut_showShortcutDialog() {
        val die = Die(10)

        composeTestRule.apply {
            setContent {
                DicePouchTheme { DiceSetEditScreen(screenState = DiceSet(
                    dice = listOf(die)))
                }
            }

            onAllNodesWithContentDescription("add_icon")[1].performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("New shortcut").assertIsDisplayed()
            onNodeWithText("1d10").assertIsDisplayed()
        }
    }

    @Test
    @Ignore("Snackbar not working because of no scaffold for now")
    //TODO: introduce scaffold for the snackbar
    fun clickNewShortcut_whenNoDice_shouldPreventShowingDialog() {
        composeTestRule.apply {
            setContent { DicePouchTheme { DiceSetEditScreen(screenState = DiceSet()) } }

            onAllNodesWithContentDescription("add_icon")[1].performClick()
            onNode(isDialog()).assertDoesNotExist()
            onNodeWithText("Creating shortcut needs a die").assertIsDisplayed()
            composeTestRule.mainClock.advanceTimeBy(4500L)
            onNodeWithText("Creating shortcut needs a die").assertDoesNotExist()
        }
    }

    @Test
    fun clickExistingShortcut_showShortcutDialog_withShortcutSettings() {
        val die = Die(10)
        val shortcut = RollShortcut(name = "a_shortcut", setting = RollSetting(die = die, modifier = -2))

        composeTestRule.apply {
            setContent {
                DicePouchTheme { DiceSetEditScreen(screenState = DiceSet(
                    dice = listOf(die), shortcuts = listOf(shortcut)))
                }
            }
            onNodeWithText(text = "a_shortcut", useUnmergedTree = true).performClick()

            onNode(matcher = isDialog()).assertIsDisplayed()
            onNodeWithText("1d10 - 2").assertIsDisplayed()
            onAllNodesWithText("a_shortcut").assertCountEquals(2) // one on the screen, one on the dialog
            onNode(matcher = isDialog(), useUnmergedTree = true).assert(
                hasAnyDescendant(hasText("a_shortcut"))
            )
        }
    }

}