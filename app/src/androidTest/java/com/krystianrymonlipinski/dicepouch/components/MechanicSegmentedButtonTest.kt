package com.krystianrymonlipinski.dicepouch.components

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.krystianrymonlipinski.dicepouch.ui.components.MechanicSegmentedButton
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MechanicSegmentedButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            DicePouchTheme {
                MechanicSegmentedButton()
            }
        }
    }

    @Test
    fun checkSelectedMechanicOption_allOfThem() {
        composeTestRule.apply {
            this.onRoot().onChildAt(1).assertIsSelected()

            this.onRoot().onChildAt(0)
                .performClick()
                .assertIsSelected()
            this.onRoot().onChildAt(1)
                .performClick()
                .assertIsSelected()
            this.onRoot().onChildAt(2)
                .performClick()
                .assertIsSelected()
        }
    }
}