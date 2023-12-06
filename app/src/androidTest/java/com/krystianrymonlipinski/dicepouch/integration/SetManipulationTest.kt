package com.krystianrymonlipinski.dicepouch.integration

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.BaseIntegrationTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@Ignore("All tests pass locally, some of the fail randomly when deployed on CI/CD tool")
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

            selectCurrentSet(getConcatenatedSetNameInput("set1"))
            selectTab("Table")
            onNodeWithText(getConcatenatedSetNameInput("set1")).assertIsDisplayed()

            selectTab("Pouch")
            selectCurrentSet(getConcatenatedSetNameInput("set2"))
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
            selectCurrentSet(getConcatenatedSetNameInput("set1"))

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
    fun addDieToSet_AndChooseItToRoll() {
        composeActivityTestRule.apply {
            prepareSetWithDie()
            onNodeWithText("20").assertIsDisplayed()

            navigateUp()
            navigateUp()
            onNodeWithText(getConcatenatedSetNameInput(EXAMPLE_SET_NAME)).performClick() //select current set
            selectTab("Table")
            onNodeWithText("20").assertIsDisplayed()

            onNodeWithText("20").performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("1d20").assertIsDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun addDieToSet_andDeleteIt() {
        composeActivityTestRule.apply {
            prepareSetWithDie()
            onNodeWithText("20").assertIsDisplayed()

            onNodeWithContentDescription("delete_set_element_icon").performClick()
            waitUntilDoesNotExist(matcher = hasText("20"), timeoutMillis = 5_000)
            onNodeWithText("20").assertDoesNotExist()
            onNodeWithText("No dice added").assertIsDisplayed()
        }

    }

    @Test
    fun addShortcutToSet_andChooseItToRoll() {
        composeActivityTestRule.apply {
            prepareSetWithShortcut()
            onNodeWithText("New shortcut").assertIsDisplayed()

            navigateUp()
            navigateUp()
            selectCurrentSet(getConcatenatedSetNameInput(EXAMPLE_SET_NAME))
            selectTab("Table")
            onNodeWithText("New shortcut").assertIsDisplayed()

            onNodeWithText("New shortcut").performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("1d20").assertIsDisplayed()
        }

    }

    @Test
    fun addShortcutToSet_andDeleteIt() {
        composeActivityTestRule.apply {
            prepareSetWithShortcut()
            onNodeWithText("New shortcut").assertIsDisplayed()

            onAllNodesWithContentDescription("delete_set_element_icon")[1].performClick()
            waitForIdle()
            onNodeWithText("1d20").assertDoesNotExist()
            onNodeWithText("No shortcuts added").assertIsDisplayed()
        }
    }

    @Test
    fun createShortcut_updateIt_andChooseItToRoll() {
        val nameInput = "_v2"

        composeActivityTestRule.apply {
            prepareSetWithShortcut()
            updateShortcut("New shortcut", nameInput)

            navigateUp()
            navigateUp()
            selectCurrentSet(getConcatenatedSetNameInput(EXAMPLE_SET_NAME))
            selectTab("Table")
            onNodeWithText(getConcatenatedShortcutNameInput(nameInput)).assertIsDisplayed()

            onNodeWithText(getConcatenatedShortcutNameInput(nameInput)).performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("2d20 - 1").assertIsDisplayed()
        }
    }


    private fun prepareSetWithDie() {
        createSet(EXAMPLE_SET_NAME)
        navigateToEditSet(getConcatenatedSetNameInput(EXAMPLE_SET_NAME))
        addDieToSet()
    }

    private fun prepareSetWithShortcut() {
        prepareSetWithDie() // every shortcut needs a die
        addShortcutToSet()
    }

}