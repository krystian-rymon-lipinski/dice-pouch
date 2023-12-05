package com.krystianrymonlipinski.dicepouch.integration

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.BaseIntegrationTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SetManipulationTest : BaseIntegrationTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        selectTab("Pouch")
        clearAllSets()
    }

    @Test
    fun createSet_updateItsName_AndDeleteIt() {
        composeActivityTestRule.apply {
            createSet(EXAMPLE_SET_NAME)
            onNodeWithText(CONCATENATED_SET_NAME_INPUT).assertIsDisplayed()

            navigateToEditSet(CONCATENATED_SET_NAME_INPUT)
            updateSetName("_v3")
            onNodeWithText("${CONCATENATED_SET_NAME_INPUT}_v3").assertIsDisplayed()
            navigateUp()
            navigateUp()
            onNodeWithText("${CONCATENATED_SET_NAME_INPUT}_v3").assertIsDisplayed()

            deleteSet("${CONCATENATED_SET_NAME_INPUT}_v3")
            onNodeWithText(CONCATENATED_SET_NAME_INPUT).assertDoesNotExist()
        }
    }

    @Test
    fun selectCurrentSet_thenDeleteIt() {
        composeActivityTestRule.apply {
            createSet("set1")
            createSet("set2")
            onNodeWithText(getConcatenatedSetNameInput("set1")).assertIsDisplayed()
            onNodeWithText(getConcatenatedSetNameInput("set2")).assertIsDisplayed()

            onNodeWithText(getConcatenatedSetNameInput("set1")).performClick()
            selectTab("Table")
            onNodeWithText(getConcatenatedSetNameInput("set1")).assertIsDisplayed()

            selectTab("Pouch")
            onNodeWithText(getConcatenatedSetNameInput("set2")).performClick()
            selectTab("Table")
            onNodeWithText(getConcatenatedSetNameInput("set2")).assertIsDisplayed()

            selectTab("Pouch")
            deleteSet(getConcatenatedSetNameInput("set2"))
            selectTab("Table")
            onNodeWithText("No dice set chosen").assertIsDisplayed()
        }
    }

    @Test
    fun navigateToEditChosenSet_withCurrentSet_andTheWithOther() {
        composeActivityTestRule.apply {
            createSet("set1")
            createSet("set2")
            onNodeWithText(getConcatenatedSetNameInput("set1")).performClick()

            navigateToEditSet(getConcatenatedSetNameInput("set1"))
            checkIfDiceSetEditScreenOn()
            onNodeWithText(getConcatenatedSetNameInput("set1")).assertIsDisplayed()

            navigateUp()
            navigateUp() // exit edit mode to enter it once again in a moment
            navigateToEditSet(getConcatenatedSetNameInput("set2"))
            checkIfDiceSetEditScreenOn()
            onNodeWithText(getConcatenatedSetNameInput("set2")).assertIsDisplayed()
        }


    }

    @Test
    fun addDieToSet_chooseItToRoll_andDeleteItAfterwards() {
        composeActivityTestRule.apply {
            createSet(EXAMPLE_SET_NAME)
            navigateToEditSet(getConcatenatedSetNameInput(EXAMPLE_SET_NAME))
            addDieToSet()
            onNodeWithText("20").assertIsDisplayed()

            navigateUp()
            navigateUp()
            onNodeWithText(getConcatenatedSetNameInput(EXAMPLE_SET_NAME)).performClick() //select current set
            selectTab("Table")
            onNodeWithText("20").assertIsDisplayed()

            onNodeWithText("20").performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("1d20").assertIsDisplayed()

            navigateBack()
            selectTab("Pouch")
            navigateToEditSet(getConcatenatedSetNameInput(EXAMPLE_SET_NAME))
            onNodeWithContentDescription("delete_set_element_icon").performClick()
            onNodeWithText("20").assertDoesNotExist()
            onNodeWithText("No dice added").assertIsDisplayed()

            navigateUp()
            selectTab("Table")
            onNodeWithText("20").assertDoesNotExist()
            onNodeWithText("No dice added").assertIsDisplayed()


        }
    }

    @Test
    fun addShortcutToSet_chooseItToRoll_thenUpdateItAndCheckForChanges_byChoosingToRollAgain_thenDeleteIt() {
        composeActivityTestRule.apply {
            createSet(EXAMPLE_SET_NAME)
            navigateToEditSet(getConcatenatedSetNameInput(EXAMPLE_SET_NAME))
            addDieToSet() // every shortcut is based on a die
            addShortcutToSet()
            onNodeWithText("New shortcut").assertIsDisplayed()

            navigateUp()
            navigateUp()
            onNodeWithText(getConcatenatedSetNameInput(EXAMPLE_SET_NAME)).performClick() //select current set
            selectTab("Table")
            onNodeWithText("New shortcut").assertIsDisplayed()

            onNodeWithText("New shortcut").performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("1d20").assertIsDisplayed()

            composeActivityTestRule.mainClock.advanceTimeBy(milliseconds = 4_000)
            dismissRollingDialog()
            selectTab("Pouch")
            navigateToEditSet(getConcatenatedSetNameInput(EXAMPLE_SET_NAME))
            onNodeWithText("New shortcut").performClick()
            onNode(isDialog()).assertIsDisplayed()
            updateShortcut("Shortcut 23")
            onNodeWithText(getConcatenatedShortcutNameInput("Shortcut 23")).assertIsDisplayed()

            navigateUp()
            selectTab("Table")
            onNodeWithText(getConcatenatedShortcutNameInput("Shortcut 23")).assertIsDisplayed()
            onNodeWithText(getConcatenatedShortcutNameInput("Shortcut 23")).performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("2d20 - 1").assertIsDisplayed()

            composeActivityTestRule.mainClock.advanceTimeBy(milliseconds = 10_000)
            dismissRollingDialog()
            selectTab("Pouch")
            navigateToEditSet(getConcatenatedSetNameInput(EXAMPLE_SET_NAME))
            onAllNodesWithContentDescription("delete_set_element_icon")[1].performClick()
            onNodeWithText("2d20 - 1").assertDoesNotExist()

            navigateUp()
            selectTab("Table")
            onNodeWithText("2d20 - 1").assertDoesNotExist()
            onNodeWithText("No shortcuts added")
        }
    }


    private fun navigateBack() {
         composeActivityTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
    }

}