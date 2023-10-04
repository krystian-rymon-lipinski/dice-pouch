package com.krystianrymonlipinski.dicepouch.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.ui.screens.RollScreen
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RollScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val shortcut = RollShortcut(name = "a_shortcut_name")

    @Before
    fun setUp() {
        composeTestRule.setContent {
            DicePouchTheme {
                RollScreen(screenState = DiceSet(
                    "A set",
                    listOf(Die(6), Die(8), Die(20)),
                    listOf(shortcut)
                ))
            }
        }
    }

    @Test
    fun rollScreen_showSettingsDialog_forSixSidesDie() {
        clickDie_showRollSettingsDialog(Die(6))
    }

    @Test
    fun rollScreen_showSettingsDialog_forTwentySidesDie() {
        clickDie_showRollSettingsDialog(Die(20))
    }

    @Test
    fun rollScreen_clickShortcut_showRollDialog() {
        composeTestRule.apply {
            onNodeWithText(text = "a_shortcut_name", useUnmergedTree = true).onParent().performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText(shortcut.setting.rollDescription).assertExists()
        }
    }

    private fun clickDie_showRollSettingsDialog(die: Die) {
        composeTestRule.apply {
            onNodeWithContentDescription(label = "d${die.sides}").performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("1d${die.sides}").assertExists()
        }
    }
}