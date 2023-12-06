package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.krystianrymonlipinski.dicepouch.BaseAndroidTest
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.PouchScreenState
import org.junit.Test

class PouchScreenTest : BaseAndroidTest() {

    @Test
    fun clickAddNewSet_showDialog() {
        composeTestRule.apply {
            setContent { PouchScreen() }

            onNodeWithText("+").performClick()
            onNode(isDialog()).assertIsDisplayed()
        }
    }

    @Test
    fun longClickExistingSet_goIntoEditMode() {
        composeTestRule.apply {
            setContent { PouchScreen(
                screenState = PouchScreenState(allSets = listOf(
                    DiceSetInfo(name = "A_set")), currentlyChosenSetId = null
                )
            ) }

            onNodeWithText("A_set").performTouchInput { longClick() }
            onNodeWithContentDescription("arrow_back").assertIsDisplayed()
            onNodeWithContentDescription("edit_set").assertIsDisplayed()
            onNodeWithContentDescription("delete_set").assertIsDisplayed()
        }
    }

    @Test
    fun clickAddNewSet_doNothing_whenInEditMode() {
        composeTestRule.apply {
            setContent { PouchScreen(
                screenState = PouchScreenState(
                    allSets = listOf(
                        DiceSetInfo(name = "A_set")
                    ), currentlyChosenSetId = null
                )
            ) }

            onNodeWithText("A_set").performTouchInput { longClick() }
            onNodeWithText("+").performClick()
            onNode(isDialog()).assertDoesNotExist()
        }
    }
}