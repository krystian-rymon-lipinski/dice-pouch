package com.krystianrymonlipinski.dicepouch.dialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollSettingsDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RollSettingsDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            DicePouchTheme {
                RollSettingsDialog(die = Die(20))
            }
        }
    }

    @Test
    fun rollSettingChange_diceNumberIncrement() {
        composeTestRule.apply {
            this
                .onAllNodesWithContentDescription("plus")[0]
                .performClick()
            this.onNodeWithText("2d20").assertIsDisplayed()
        }
    }

    @Test
    fun rollSettingChange_diceNumberDecrement() {
        composeTestRule.apply {
            this
                .onAllNodesWithContentDescription("minus")[0]
                .performClick()
            this.onNodeWithText("30d20").assertIsDisplayed()
        }
    }

    @Test
    fun rollSettingChange_modifierIncrement() {
        composeTestRule.apply {
            this
                .onAllNodesWithContentDescription("plus")[1]
                .performClick()
            this.onNodeWithText("1d20 + 1").assertIsDisplayed()
        }
    }

    @Test
    fun rollSettingChange_modifierDecrement() {
        composeTestRule.apply {
            this
                .onAllNodesWithContentDescription("minus")[1]
                .performClick()
            this.onNodeWithText("1d20 - 1").assertIsDisplayed()
        }
    }

    @Test
    fun rollSettingChange_mechanicChanges() {
        composeTestRule.apply {
            this
                .onAllNodesWithContentDescription("mechanics_option")[0]
                .performClick()
            this.onNodeWithText("1d20 (D)").assertIsDisplayed()

            this
                .onAllNodesWithContentDescription("mechanics_option")[1]
                .performClick()
            this.onNodeWithText("1d20").assertIsDisplayed()

            this
                .onAllNodesWithContentDescription("mechanics_option")[2]
                .performClick()
            this.onNodeWithText("1d20 (A)").assertIsDisplayed()
        }
    }
}