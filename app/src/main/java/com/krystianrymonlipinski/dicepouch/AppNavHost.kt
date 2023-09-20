package com.krystianrymonlipinski.dicepouch

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.krystianrymonlipinski.dicepouch.ui.screens.DiceSetEditScreen
import com.krystianrymonlipinski.dicepouch.ui.screens.RollScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_ROLL_SCREEN
    ) {
        composable(route = ROUTE_ROLL_SCREEN) {
            RollScreen(onEditIconClicked = { navController.navigate(ROUTE_DICE_SET_EDIT) })
        }
        composable(route = ROUTE_DICE_SET_EDIT) {
            DiceSetEditScreen()
        }
    }
}

const val ROUTE_ROLL_SCREEN = "roll_screen_route"
const val ROUTE_DICE_SET_EDIT = "dice_set_edit_route"