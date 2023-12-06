package com.krystianrymonlipinski.dicepouch.ui.dialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.BaseAndroidTest
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Test

class NewDieDialogTest : BaseAndroidTest() {


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

    @Test
    fun newDieDialog_stateRestoration() {
        restorationTester.setContent {
            DicePouchTheme { NewDieDialog(onDialogDismissed = { }, onNewDieAdded = { }) }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("plus").performClick()
            onNodeWithText("21").assertIsDisplayed()
            restorationTester.emulateSavedInstanceStateRestore()
            onNodeWithText("21").assertIsDisplayed()
        }
    }
}