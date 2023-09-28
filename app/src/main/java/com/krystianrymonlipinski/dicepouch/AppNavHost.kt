package com.krystianrymonlipinski.dicepouch

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.krystianrymonlipinski.dicepouch.ui.screens.DiceSetEditRoute
import com.krystianrymonlipinski.dicepouch.ui.screens.RollRoute

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_ROLL_SCREEN,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = ROUTE_ROLL_SCREEN) {
            RollRoute(onEditIconClicked = { navController.navigate(ROUTE_DICE_SET_EDIT) })
        }
        composable(route = ROUTE_DICE_SET_EDIT) {
            DiceSetEditRoute()
        }
    }
}

const val ROUTE_ROLL_SCREEN = "roll_screen_route"
const val ROUTE_DICE_SET_EDIT = "dice_set_edit_route"