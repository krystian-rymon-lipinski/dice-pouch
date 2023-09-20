package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AppNavHostTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            AppNavHost(navController = navController)
        }
    }

    @Test
    fun appNavigation_startDestination() {
        composeTestRule.onNodeWithText("Basic D&D Set").assertIsDisplayed()
        assertEquals(ROUTE_ROLL_SCREEN, navController.currentDestination?.route)
    }

    @Test
    fun appNavigation_navigateTo_diceSetEditScreen() {
        composeTestRule.onNodeWithContentDescription("edit_set_icon").performClick()
        assertEquals(ROUTE_DICE_SET_EDIT, navController.currentDestination?.route)
    }
}