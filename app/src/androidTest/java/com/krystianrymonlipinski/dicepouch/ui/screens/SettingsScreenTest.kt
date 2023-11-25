package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.BaseAndroidTest
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Before
import org.junit.Test

class SettingsScreenTest : BaseAndroidTest() {

    @Before
    fun setUp() {
        composeTestRule.setContent {
            DicePouchTheme {
                SettingsScreen(settingsScreenState = RollingSettings())
            }
        }
    }


    @Test
    fun checkAutodismissRollPopup() {
        composeTestRule.apply {
            onNodeWithTag(testTag = "popup_autodismiss_switch").performClick()
            onNodeWithText(text = "After: 500 ms", substring = true).assertIsDisplayed()

            onNodeWithTag(testTag = "popup_autodismiss_switch").performClick()
            onNodeWithText(text = "After: 500 ms", substring = true).assertDoesNotExist()
        }

    }

    //TODO: improve; write tests for sliders
}