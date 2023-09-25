package com.krystianrymonlipinski.dicepouch.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
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

            onNodeWithContentDescription("add_die_icon").performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("sides").assertIsDisplayed()
        }
    }

    @Test
    fun deleteDie() {
        composeTestRule.apply {
            setContent { DicePouchTheme {
                DiceSetEditScreen(screenState = DiceSet(dice = listOf(Die(4), Die(6), Die(8))))
            } }

            onAllNodesWithContentDescription("delete_die_icon")[1].performClick()
            onAllNodesWithContentDescription("delete_die_icon").assertCountEquals(2)
        }
    }
}