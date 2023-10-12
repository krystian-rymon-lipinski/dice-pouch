package com.krystianrymonlipinski.dicepouch

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.krystianrymonlipinski.dicepouch.ui.screens.DiceSetEditRoute
import com.krystianrymonlipinski.dicepouch.ui.screens.PouchRoute
import com.krystianrymonlipinski.dicepouch.ui.screens.RollRoute

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_ROLL_SCREEN,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = ROUTE_ROLL_SCREEN) {
            RollRoute(
                onTabClicked = { tabIndex -> if (tabIndex == 1) {
                    navController.navigate(ROUTE_POUCH_SCREEN)
                } },
                onEditIconClicked = { navController.navigate(ROUTE_DICE_SET_EDIT) })
        }
        composable(route = ROUTE_POUCH_SCREEN) {
            PouchRoute(
                onTabClicked = { tabIndex -> if (tabIndex == 0) {
                    navController.navigate(ROUTE_ROLL_SCREEN)
                } },
                onEditSetClicked = {
                    navController.navigate(ROUTE_DICE_SET_EDIT)
                }
            )
        }
        composable(route = ROUTE_DICE_SET_EDIT) {
            DiceSetEditRoute(
                onUpClicked = { navController.navigateUp() }
            )
        }
    }
}

const val ROUTE_ROLL_SCREEN = "roll_screen_route"
const val ROUTE_POUCH_SCREEN = "pouch_screen_route"
const val ROUTE_DICE_SET_EDIT = "dice_set_edit_route"