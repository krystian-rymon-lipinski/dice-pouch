package com.krystianrymonlipinski.dicepouch.dialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.BaseAndroidTest
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollSettingsDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Test

class RollSettingsDialogTest : BaseAndroidTest() {


    @Test
    fun rollSettingChange_clickAllPossibleOptions() {
        composeTestRule.apply {
            setContent {
                DicePouchTheme { RollSettingsDialog(die = Die(20)) }
            }

            onAllNodesWithContentDescription("plus")[0]
                .performClick()
                .performClick()
            onAllNodesWithContentDescription("minus")[0]
                .performClick()
            onAllNodesWithContentDescription("plus")[1]
                .performClick()
            onAllNodesWithContentDescription("minus")[1]
                .performClick()
                .performClick()

            onAllNodesWithContentDescription("mechanics_option")[0].performClick()
            onNodeWithText("2d20 - 1 (D)").assertIsDisplayed()
            onAllNodesWithContentDescription("mechanics_option")[1].performClick()
            onNodeWithText("2d20 - 1").assertIsDisplayed()
            onAllNodesWithContentDescription("mechanics_option")[2].performClick()
            onNodeWithText("2d20 - 1 (A)").assertIsDisplayed()
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

}