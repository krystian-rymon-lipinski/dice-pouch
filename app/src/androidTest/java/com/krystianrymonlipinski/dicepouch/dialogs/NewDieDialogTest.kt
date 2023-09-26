package com.krystianrymonlipinski.dicepouch.dialogs

import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.krystianrymonlipinski.dicepouch.ui.dialogs.NewDieDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Rule
import org.junit.Test

class NewDieDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun checkControlIcons() {
        composeTestRule.apply {
            setContent { DicePouchTheme { NewDieDialog(onDialogDismissed = { }, onNewDieAdded = { } ) } }

            onNodeWithText("20").assertIsDisplayed()
            onNodeWithContentDescription("plus").performClick()
            onNodeWithText("21").assertIsDisplayed()
            onNodeWithContentDescription("minus").performClick()
            onNodeWithText("20").assertIsDisplayed()

            onNodeWithContentDescription("die_sides_slider").performTouchInput {
                slideOutHorizontally { fullWidth -> fullWidth }
            }
            onNodeWithText("100")
        }
    }

    @Test
    fun checkDieSidesIcons_ifDisabled() {
        composeTestRule.apply {
            setContent { DicePouchTheme { NewDieDialog(onDialogDismissed = { }, onNewDieAdded = { }) } }

            for (i in 1.. 80) {
                onNodeWithContentDescription("plus").performClick()
            }
            onNodeWithContentDescription(useUnmergedTree = true, label = "plus").onParent().assertIsNotEnabled()
            for (i in 1.. 97) {
                onNodeWithContentDescription("minus").performClick()
            }
            onNodeWithContentDescription(useUnmergedTree = true, label = "minus").onParent().assertIsNotEnabled()
        }
    }
}