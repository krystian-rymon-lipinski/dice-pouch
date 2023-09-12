package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.ui.screens.RollScreen
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RollScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            DicePouchTheme {
                RollScreen()
            }
        }
    }

    @Test
    fun rollScreen_showSettingsDialog_forSixSidesDie() {
        clickDieAnTestShowingDialog(Die(6))
    }

    @Test
    fun rollScreen_showSettingsDialog_forTwentySidesDie() {
        clickDieAnTestShowingDialog(Die(20))
    }

    private fun clickDieAnTestShowingDialog(die: Die) {
        composeTestRule.apply {
            this
                .onNodeWithContentDescription(label = "d${die.sides}")
                .performClick()
            this.onNode(isDialog())
                .assertExists()
                .assertIsDisplayed()
            this.onNodeWithText("1d${die.sides}").assertExists()
        }
    }
}