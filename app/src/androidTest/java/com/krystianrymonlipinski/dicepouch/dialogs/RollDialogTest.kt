package com.krystianrymonlipinski.dicepouch.dialogs

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import com.krystianrymonlipinski.dicepouch.BaseAndroidTest
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Test

class RollDialogTest : BaseAndroidTest() {


    @Test
    fun rollingTest_normalMechanic_singleDie() {
        setDialogWithSetting(setting = RollSetting(Die(8), 1, 0))
        composeTestRule.apply {
            this.onNodeWithText("1d8").assertIsDisplayed()
            this.onAllNodesWithContentDescription("d8").assertCountEquals(1)
        }

    }

    @Test
    fun rollingTest_normalMechanic_singleDie_withModifier() {
        setDialogWithSetting(setting = RollSetting(Die(8), 1, -1))
        composeTestRule.apply {
            this.onNodeWithText("1d8 - 1").assertIsDisplayed()
            this.onNodeWithText("-1").assertIsDisplayed()
            this.onAllNodesWithContentDescription("d8").assertCountEquals(1)
        }

    }

    @Test
    fun rollingTest_normalMechanic_multipleDice() {
        setDialogWithSetting(setting = RollSetting(Die(8), 6, 0))
        composeTestRule.apply {
            this.onNodeWithText("6d8").assertIsDisplayed()

            this.onNodeWithContentDescription("dice_sum_scrollable_row").performScrollToIndex(5)

            this.onAllNodesWithContentDescription("d8").assertCountEquals(6)
        }
    }

    @Test
    fun rollingTest_normalMechanic_multipleDice_withModifier() {
        setDialogWithSetting(setting = RollSetting(Die(8), 6, 3))
        composeTestRule.apply {
            this.onNodeWithText("6d8 + 3").assertIsDisplayed()
            this.onNodeWithText("3").assertIsDisplayed()

            this.onNodeWithContentDescription("dice_sum_scrollable_row").performScrollToIndex(5)

            this.onAllNodesWithContentDescription("d8").assertCountEquals(6)
        }
    }

    @Test
    fun rollingTest_advantageMechanic_singleDie() {
        setDialogWithSetting(setting = RollSetting(
            die = Die(8),
            mechanic = RollSetting.Mechanic.ADVANTAGE
        ))
        composeTestRule.apply {
            this.onNodeWithText("1d8 (A)").assertIsDisplayed()
            this.onAllNodesWithContentDescription("d8").assertCountEquals(2)
        }
    }

    @Test
    fun rollingTest_advantageMechanic_singleDie_withModifier() {
        setDialogWithSetting(setting = RollSetting(
            die = Die(8),
            modifier = -2,
            mechanic = RollSetting.Mechanic.ADVANTAGE
        ))
        composeTestRule.apply {
            this.onNodeWithText("1d8 - 2 (A)").assertIsDisplayed()
            this.onAllNodesWithText("-2").assertCountEquals(2)
            this.onAllNodesWithContentDescription("d8").assertCountEquals(2)
        }
    }

    @Test
    fun rollingTest_advantageMechanic_multipleDice() {
        setDialogWithSetting(setting = RollSetting(
            die = Die(8),
            diceNumber = 4,
            mechanic = RollSetting.Mechanic.ADVANTAGE
        ))
        composeTestRule.apply {
            this.onNodeWithText("4d8 (A)").assertIsDisplayed()

            this.onAllNodesWithContentDescription("dice_sum_scrollable_row")[0].performScrollToIndex(3)
            this.onAllNodesWithContentDescription("dice_sum_scrollable_row")[1].performScrollToIndex(3)

            this.onAllNodesWithContentDescription("d8").assertCountEquals(8)
        }
    }

    @Test
    fun rollingTest_advantageMechanic_multipleDice_withModifier() {
        setDialogWithSetting(setting = RollSetting(
            die = Die(8),
            diceNumber = 5,
            modifier = -4,
            mechanic = RollSetting.Mechanic.ADVANTAGE
        ))
        composeTestRule.apply {
            this.onNodeWithText("5d8 - 4 (A)").assertIsDisplayed()
            this.onAllNodesWithText("-4").assertCountEquals(2)

            this.onAllNodesWithContentDescription("dice_sum_scrollable_row")[0].performScrollToIndex(4)
            this.onAllNodesWithContentDescription("dice_sum_scrollable_row")[1].performScrollToIndex(4)

            this.onAllNodesWithContentDescription("d8").assertCountEquals(10)
        }
    }

    private fun setDialogWithSetting(setting: RollSetting) {
        composeTestRule.setContent {
            DicePouchTheme {
                RollDialog(setting = setting)
            }
        }
    }

}