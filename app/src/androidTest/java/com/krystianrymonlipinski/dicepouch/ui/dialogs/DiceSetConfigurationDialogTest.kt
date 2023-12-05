package com.krystianrymonlipinski.dicepouch.ui.dialogs

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import com.krystianrymonlipinski.dicepouch.BaseAndroidTest
import org.junit.Test

class DiceSetConfigurationDialogTest : BaseAndroidTest() {

    @Test
    fun changeSetName() {
        composeTestRule.apply {
            setContent { DiceSetConfigurationDialog() }

            onNode(hasImeAction(ImeAction.Default)).performClick()
            onNode(hasImeAction(ImeAction.Default)).performTextInput("bcd")
            onAllNodesWithText("New setbcd").assertCountEquals(2)
        }
    }


}