package com.krystianrymonlipinski.dicepouch.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
        composeTestRule.onNodeWithText("Basic D&D Set").assertIsDisplayed()
    }

    @Test
    fun appNavigation_navigateToAndFrom_diceSetEditScreen() {
        composeTestRule.apply {
            onNodeWithContentDescription("edit_set_icon").performClick()
            onNodeWithText("Basic D&D Set").assertDoesNotExist()

            activityRule.scenario.onActivity {
                it.onBackPressedDispatcher.onBackPressed()
            }
            onNodeWithText("Basic D&D Set").assertIsDisplayed()
        }


    }
}