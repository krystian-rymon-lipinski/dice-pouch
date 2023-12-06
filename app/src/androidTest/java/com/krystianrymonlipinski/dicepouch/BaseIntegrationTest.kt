package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.text.input.ImeAction
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
open class BaseIntegrationTest {


    @get:Rule(order = 1)
    val composeActivityTestRule by lazy { createAndroidComposeRule<MainActivity>() }
    

    protected fun getConcatenatedSetNameInput(input: String) = "New set$input"
    protected fun getConcatenatedShortcutNameInput(input: String) = "New shortcut$input"

    protected fun selectTab(tab: String) {
        composeActivityTestRule.onNodeWithText(tab).performClick()
    }


    protected fun createSet(setName: String) {
        composeActivityTestRule.apply {
            onAllNodesWithText("+")[0].performClick() // hot-fixed: test sees here 2 cards with "+", choose the first one
            onAllNodesWithText("New set")[0].performClick()
            onAllNodesWithText("New set")[0].performTextInput(setName)
            onNodeWithText("Add").performClick()
        }
    }

    protected fun updateSetName(newName: String) {
        composeActivityTestRule.apply {
            onNodeWithContentDescription("edit_set").performClick()
            onNode(hasImeAction(ImeAction.Default)).performClick()
            onNode(hasImeAction(ImeAction.Default)).performTextInput(newName)
            onNodeWithText("Save").performClick()
        }
    }

    protected fun selectCurrentSet(setName: String) {
        composeActivityTestRule.onNodeWithText(setName).performClick()
    }

    protected fun deleteSet(setName: String) {
        composeActivityTestRule.apply {
            onNodeWithText(setName).performTouchInput { longClick() }
            onNodeWithContentDescription("delete_set").performClick()
        }
    }

    protected fun navigateToEditSet(setName: String) {
        composeActivityTestRule.apply {
            onNodeWithText(setName).performTouchInput { longClick() }
            onNodeWithContentDescription("edit_set").performClick()
        }
    }

    protected fun addDieToSet() {
        composeActivityTestRule.apply {
            onAllNodesWithContentDescription("add_set_element_icon")[0].performClick()
            onNodeWithText("Add").performClick()
        }
    }

    protected fun addShortcutToSet() {
        composeActivityTestRule.apply {
            onAllNodesWithContentDescription("add_set_element_icon")[1].performClick()
            onNodeWithText("Add").performClick()
        }
    }

    protected fun updateShortcut(oldName: String, input: String) {
        composeActivityTestRule.apply {
            onNodeWithText(oldName).performClick()
            onNode(hasImeAction(ImeAction.Default)).performClick()
            onNode(hasImeAction(ImeAction.Default)).performTextInput(input)
            onAllNodesWithContentDescription("plus")[0].performClick()
            onAllNodesWithContentDescription("minus")[1].performClick()
            onNodeWithText("Save").performClick()
        }
    }

    protected fun navigateUp() {
        composeActivityTestRule.onNodeWithContentDescription("arrow_back").performClick()
    }

    protected fun clearAllSets() {
        composeActivityTestRule.apply {
            onNodeWithText("Pouch").performClick()
            val existingSets = onNodeWithText("+").onSiblings()
                .fetchSemanticsNodes(atLeastOneRootRequired = false)
            for (i in existingSets.indices) {
                onNodeWithText("+").onSiblings()[i].performTouchInput { longClick() }
                onNodeWithContentDescription("delete_set").performClick()
            }
        }
    }

    protected fun checkIfDiceSetEditScreenOn() {
        composeActivityTestRule.apply {
            onNodeWithContentDescription("arrow_back").assertIsDisplayed()
            onNodeWithContentDescription("edit_set").assertIsDisplayed()
            onNodeWithContentDescription("delete_set").assertDoesNotExist()
        }
    }


    @HiltAndroidTest
    companion object {
        @JvmStatic protected val EXAMPLE_SET_NAME = "_v2"
        @JvmStatic protected val CONCATENATED_SET_NAME_INPUT = "New set$EXAMPLE_SET_NAME"
    }
}