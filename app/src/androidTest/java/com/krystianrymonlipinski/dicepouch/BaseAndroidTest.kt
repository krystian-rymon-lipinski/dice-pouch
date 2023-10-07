package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

open class BaseAndroidTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    protected val restorationTester = StateRestorationTester(composeTestRule)
}