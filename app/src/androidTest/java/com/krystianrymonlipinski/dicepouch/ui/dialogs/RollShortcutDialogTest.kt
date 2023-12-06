package com.krystianrymonlipinski.dicepouch.ui.dialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.krystianrymonlipinski.dicepouch.BaseAndroidTest
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Test

class RollShortcutDialogTest : BaseAndroidTest() {


    @Test
    fun shortcutDialog_newShortcut() {
        composeTestRule.apply {
            setContent { DicePouchTheme { RollShortcutDialog(
                currentShortcut = null,
                diceInSet = listOf(Die(6), Die(10))
            )}}

            onNodeWithText("1d6").assertIsDisplayed()
            onNodeWithText("New shortcut").assertIsDisplayed()
            onNodeWithText("6").assertIsDisplayed()
        }
    }

    @Test
    fun shortcutDialog_shortcutToUpdate() {
        val dieInShortcut = Die(20)
        composeTestRule.apply {
            setContent { DicePouchTheme { RollShortcutDialog(
                currentShortcut = RollShortcut(
                    name = "Shortcut name",
                    setting = RollSetting(die = dieInShortcut, modifier = -4)
                ),
                diceInSet = listOf(Die(8), dieInShortcut)
            )}}

            onNodeWithText("1d20 - 4")
            onNodeWithText("Shortcut name").assertIsDisplayed()
            onNodeWithText("20").assertIsDisplayed()
        }
    }

    @Test
    fun rollShortcut_changeName() {
        composeTestRule.apply {
            setContent { DicePouchTheme { RollShortcutDialog(
                    currentShortcut = null,
                    diceInSet = listOf(Die(6))
                )
            } }

            onNodeWithText("New shortcut")
                .performClick()
                .performTextInput(" dede")
            onNodeWithText("New shortcut dede").assertIsDisplayed()
        }
    }

    @Test
    fun rollShortcut_changeSettings_checkAllControls() {
        composeTestRule.apply {
            setContent { DicePouchTheme { RollShortcutDialog(
                currentShortcut = null,
                diceInSet = listOf(Die(8), Die(12), Die(15))
            )}}
            onNodeWithText("1d8").assertIsDisplayed()

            onNodeWithContentDescription("arrow_left")
                .performClick()
                .performClick()
            onNodeWithContentDescription("arrow_right")
                .performClick()
            onAllNodesWithContentDescription("plus")[0]
                .performClick()
            onAllNodesWithContentDescription("minus")[0]
                .performClick()
                .performClick()
            onAllNodesWithContentDescription("plus")[1]
                .performClick()
                .performClick()
                .performClick()
            onAllNodesWithContentDescription("minus")[1]
                .performClick()
            onAllNodesWithContentDescription("mechanics_option")[2]
                .performClick()

            onNodeWithText("30d15 + 2 (A)").assertIsDisplayed()
        }
    }

    @Test
    fun rollShortcutDialog_saveButtonDisabled_whenNameBlank() {
        composeTestRule.apply {
            setContent { DicePouchTheme { RollShortcutDialog(
                currentShortcut = null, diceInSet = listOf(Die(20))) } }

            onNodeWithText("Add").assertIsEnabled()
            onNodeWithText("New shortcut").performTextReplacement("  ")
            onNodeWithText("Add").assertIsNotEnabled()
        }
    }


    @Test
    fun rollShortcutDialog_restoreState() {
        restorationTester.setContent { DicePouchTheme { RollShortcutDialog(
            currentShortcut = null,
            diceInSet = listOf(Die(8), Die(12), Die(15))
        )}}

        composeTestRule.apply {
            onNodeWithContentDescription("arrow_right").performClick()
            onAllNodesWithContentDescription("plus")[0].performClick()
            onAllNodesWithContentDescription("plus")[1].performClick()
            onAllNodesWithContentDescription("mechanics_option")[0].performClick()

            onNodeWithText("2d12 + 1 (D)").assertIsDisplayed()
            restorationTester.emulateSavedInstanceStateRestore()
            onNodeWithText("2d12 + 1 (D)").assertIsDisplayed()
        }
    }



}