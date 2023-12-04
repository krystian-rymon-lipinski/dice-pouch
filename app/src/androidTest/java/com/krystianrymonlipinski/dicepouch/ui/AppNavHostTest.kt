package com.krystianrymonlipinski.dicepouch.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import com.krystianrymonlipinski.dicepouch.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AppNavHostTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Test
    fun appNavigation_startDestination() {
        checkIfTabSelected("Table")
    }

    @Test
    fun appNavigation_navigateThroughTabs() {
        composeTestRule.apply {
            onNodeWithText("Pouch").performClick()
            checkIfTabSelected("Pouch")

            activityRule.scenario.onActivity {
                it.onBackPressedDispatcher.onBackPressed()
            }
            checkIfTabSelected("Table")

            onNodeWithText("Settings").performClick()
            checkIfTabSelected("Settings")

            activityRule.scenario.onActivity {
                it.onBackPressedDispatcher.onBackPressed()
            }
            checkIfTabSelected("Table")

            onNodeWithText("Pouch").performClick()
            checkIfTabSelected("Pouch")

            onNodeWithText("Settings").performClick()
            checkIfTabSelected("Settings")

            onNodeWithText("Table").performClick()
            checkIfTabSelected("Table")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun appNavigation_navigateToEditSetScreen_andBack_byOnBackPressed() = runTest {
        composeTestRule.apply {
            onNodeWithText("Pouch").performClick()
            onNodeWithContentDescription("edit_set").assertDoesNotExist()

            createNewSet() //to ensure there is at least one sibling
                onNode(isRoot()).printToLog("log before")
            onNodeWithText("+").onSiblings()[0].performTouchInput { longClick() }
            checkIfPouchScreenIsInEditMode()

            onNodeWithContentDescription("edit_set").performClick()
            checkIfDiceSetEditScreenOn()

            activityRule.scenario.onActivity {
                it.onBackPressedDispatcher.onBackPressed()
            }
            checkIfTabSelected("Pouch")
            checkIfPouchScreenIsInEditMode()
                onNode(isRoot()).printToLog("log after")
            deleteSet() // reset app state to before this test
        }
    }

    @Test
    fun appNavigation_navigateToEditSetScreen_andBack_byUp() {
        composeTestRule.apply {
            onNodeWithText("Pouch").performClick()
            onNodeWithContentDescription("edit_set").assertDoesNotExist()

            createNewSet() //to be sure that a node with chosen text exists
            onNodeWithText("+").onSiblings()[0].performTouchInput { longClick() }
            checkIfPouchScreenIsInEditMode()

            onNodeWithContentDescription("edit_set").performClick()
            checkIfDiceSetEditScreenOn()

            onNodeWithContentDescription("arrow_back").performClick()

            checkIfTabSelected("Pouch")
            checkIfPouchScreenIsInEditMode()
            deleteSet() // reset app state to before this test
        }
    }

    private fun checkIfTabSelected(tab: String) {
        composeTestRule.apply {
            onNodeWithText(tab)
                .assertIsDisplayed()
                .assertIsSelected()
        }
    }

    private fun checkIfPouchScreenIsInEditMode() {
        composeTestRule.apply {
            onNodeWithContentDescription("arrow_back").assertIsDisplayed()
            onNodeWithContentDescription("edit_set").assertIsDisplayed()
            onNodeWithContentDescription("delete_set").assertIsDisplayed()
        }
    }

    private fun checkIfDiceSetEditScreenOn() {
        composeTestRule.apply {
            onNodeWithContentDescription("arrow_back").assertIsDisplayed()
            onNodeWithContentDescription("edit_set").assertIsDisplayed()
            onNodeWithContentDescription("delete_set").assertDoesNotExist()
        }
    }

    private fun createNewSet() {
        /* Assuming we're currently on Pouch tab */
        composeTestRule.apply {
            onNodeWithText("+").performClick()
            onAllNodesWithText("New set")[0].performClick()
            onAllNodesWithText("New set")[0].performTextInput(EXAMPLE_SET_NAME)
            onNodeWithText("Add").performClick()
        }
    }

    private fun deleteSet() {
        composeTestRule.apply {
            onNodeWithContentDescription("arrow_back").performClick()
            onNodeWithText(CONCATENATED_SET_NAME_INPUT).performTouchInput { longClick() }
            onNodeWithContentDescription("delete_set").performClick()
        }
    }

    companion object {
        private const val EXAMPLE_SET_NAME = "_Set 12345"
        private const val CONCATENATED_SET_NAME_INPUT = "New set$EXAMPLE_SET_NAME"
    }

}