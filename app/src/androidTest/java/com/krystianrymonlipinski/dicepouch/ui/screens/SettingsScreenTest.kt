package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
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
            onNodeWithContentDescription("popup_autodismiss_switch").performClick()
            onNodeWithText(text = "after finishing roll", substring = true).assertIsDisplayed()

            onNodeWithContentDescription("popup_autodismiss_switch").performClick()
            onNodeWithText(text = "after finishing roll", substring = true).assertDoesNotExist()
        }
    }

}