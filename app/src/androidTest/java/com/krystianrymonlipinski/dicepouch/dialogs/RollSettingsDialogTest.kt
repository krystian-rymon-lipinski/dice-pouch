package com.krystianrymonlipinski.dicepouch.dialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollSettingsDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Rule
import org.junit.Test

class RollSettingsDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun rollSettingChange_diceNumberIncrement() {
        setupDialog()
        composeTestRule.apply {
            onAllNodesWithContentDescription("plus")[0].performClick()
            onNodeWithText("2d20").assertIsDisplayed()
        }
    }

    @Test
    fun rollSettingChange_diceNumberDecrement() {
        setupDialog()
        composeTestRule.apply {
            onAllNodesWithContentDescription("minus")[0].performClick()
            onNodeWithText("30d20").assertIsDisplayed()
        }
    }

    @Test
    fun rollSettingChange_modifierIncrement() {
        setupDialog()
        composeTestRule.apply {
            onAllNodesWithContentDescription("plus")[1].performClick()
            onNodeWithText("1d20 + 1").assertIsDisplayed()
        }
    }

    @Test
    fun rollSettingChange_modifierDecrement() {
        setupDialog()
        composeTestRule.apply {
            onAllNodesWithContentDescription("minus")[1].performClick()
            onNodeWithText("1d20 - 1").assertIsDisplayed()
        }
    }

    @Test
    fun rollSettingChange_mechanicChanges() {
        setupDialog()
        composeTestRule.apply {
            onAllNodesWithContentDescription("mechanics_option")[0].performClick()
            onNodeWithText("1d20 (D)").assertIsDisplayed()

            onAllNodesWithContentDescription("mechanics_option")[1].performClick()
            onNodeWithText("1d20").assertIsDisplayed()

            onAllNodesWithContentDescription("mechanics_option")[2].performClick()
            onNodeWithText("1d20 (A)").assertIsDisplayed()
        }
    }

    @Test
    fun rollSettingsChange_stateRestoration() {
        val restorationTester = StateRestorationTester(composeTestRule).apply {
            setContent {
                DicePouchTheme { RollSettingsDialog(die = Die(20)) }
            }
        }

        composeTestRule.apply {
            onAllNodesWithContentDescription("plus")[0].performClick()
            onAllNodesWithContentDescription("minus")[1].performClick()
            onAllNodesWithContentDescription("mechanics_option")[2].performClick()

            onNodeWithText("2d20 - 1 (A)").assertIsDisplayed()
            restorationTester.emulateSavedInstanceStateRestore()
            onNodeWithText("2d20 - 1 (A)").assertIsDisplayed()
        }
    }

    private fun setupDialog() {
        composeTestRule.setContent {
            DicePouchTheme { RollSettingsDialog(die = Die(20)) }
        }
    }
}