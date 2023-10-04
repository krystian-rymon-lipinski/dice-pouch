package com.krystianrymonlipinski.dicepouch.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.ui.screens.DiceSetEditScreen
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Rule
import org.junit.Test

class DiceSetEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showAddNewDieDialog() {
        composeTestRule.apply {
            setContent { DicePouchTheme { DiceSetEditScreen() } }

            onAllNodesWithContentDescription("add_icon")[0].performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("sides").assertIsDisplayed()
        }
    }
}