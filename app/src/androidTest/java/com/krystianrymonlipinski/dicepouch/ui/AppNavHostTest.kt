package com.krystianrymonlipinski.dicepouch.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
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
        checkIfTableScreenOn()
    }

    @Test
    fun appNavigation_navigateThroughTabs() {
        composeTestRule.apply {
            onNodeWithText("Pouch").performClick()
            checkIfPouchScreenOn()

            activityRule.scenario.onActivity {
                it.onBackPressedDispatcher.onBackPressed()
            }
            checkIfTableScreenOn()

            onNodeWithText("Settings").performClick()
            checkIfSettingsScreenOn()

            activityRule.scenario.onActivity {
                it.onBackPressedDispatcher.onBackPressed()
            }
            checkIfTableScreenOn()

            onNodeWithText("Pouch").performClick()
            checkIfPouchScreenOn()

            onNodeWithText("Settings").performClick()
            checkIfSettingsScreenOn()

            onNodeWithText("Table").performClick()
            checkIfTableScreenOn()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun appNavigation_navigateToEditSetScreen_andBack_byOnBackPressed() = runTest {
        composeTestRule.apply {
            onNodeWithText("Pouch").performClick()
            onNodeWithContentDescription("edit_set").assertDoesNotExist()

            onNodeWithText(text = "Basic set", useUnmergedTree = true).onParent().performTouchInput { longClick() }
            awaitIdle()

            onNodeWithContentDescription("edit_set").assertIsDisplayed()

            onNodeWithContentDescription("edit_set").performClick()
            checkIfDiceSetEditScreenOn()

            activityRule.scenario.onActivity {
                it.onBackPressedDispatcher.onBackPressed()
            }
            checkIfPouchScreenOn()
        }
    }

    @Test
    fun appNavigation_navigateToEditSetScreen_andBack_byUp() {
        composeTestRule.apply {
            onNodeWithText("Pouch").performClick()
            onNodeWithContentDescription("edit_set").assertDoesNotExist()

            onNodeWithText("Basic set").performTouchInput { longClick(durationMillis = 1000L) }
            onNodeWithContentDescription("edit_set").assertIsDisplayed()

            onNodeWithContentDescription("edit_set").performClick()
            checkIfDiceSetEditScreenOn()

            onNodeWithContentDescription("arrow_back").performClick()
            checkIfPouchScreenOn()
        }
    }

    private fun checkIfTableScreenOn() {
        composeTestRule.apply {
            onNodeWithText("Table")
                .assertIsDisplayed()
                .assertIsSelected()
        }
    }

    private fun checkIfPouchScreenOn() {
        composeTestRule.apply {
            onNodeWithText("Pouch")
                .assertIsDisplayed()
                .assertIsSelected()
        }
    }

    private fun checkIfSettingsScreenOn() {
        composeTestRule.apply {
            onNodeWithText("Settings")
                .assertIsDisplayed()
                .assertIsSelected()
        }
    }

    private fun checkIfDiceSetEditScreenOn() {
        composeTestRule.apply {
            onNodeWithContentDescription("arrow_back").assertIsDisplayed()
            onNodeWithText("Basic set").assertIsDisplayed()
            onAllNodesWithContentDescription("secondary_caption_icon").assertCountEquals(2)
        }
    }
}