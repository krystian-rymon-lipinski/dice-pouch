package com.krystianrymonlipinski.dicepouch.ui

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.krystianrymonlipinski.dicepouch.ui.screens.DiceSetEditRoute
import com.krystianrymonlipinski.dicepouch.ui.screens.PouchRoute
import com.krystianrymonlipinski.dicepouch.ui.screens.SettingsRoute
import com.krystianrymonlipinski.dicepouch.ui.screens.TableRoute
import com.krystianrymonlipinski.dicepouch.viewmodels.MainActivityViewModel

@Composable
fun AppNavHost(
    viewModel: MainActivityViewModel = hiltViewModel(),
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_TABLE_SCREEN,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = ROUTE_TABLE_SCREEN) {
            TableRoute(
                viewModel = viewModel,
                onTabClicked = { tabIndex -> navController.navigate(getRouteFromTabIndex(tabIndex)) }
            )
        }
        composable(route = ROUTE_POUCH_SCREEN) {
            PouchRoute(
                viewModel = viewModel,
                onTabClicked = { tabIndex -> navController.navigate(getRouteFromTabIndex(tabIndex)) },
                onBackStackPopped = { navController.popBackStack() },
                onEditSetClicked = { setInfo ->
                    navController.navigate("$ROUTE_DICE_SET_EDIT/${setInfo.id}")
                }
            )
        }
        composable(
            route = "$ROUTE_DICE_SET_EDIT/{setId}",
            arguments = listOf(navArgument(name = "setId") { type = NavType.IntType })
        ) { backStackEntry ->
            DiceSetEditRoute(
                viewModel = viewModel,
                chosenSetId = backStackEntry.arguments?.getInt("setId") ?: 0,
                onUpClicked = { navController.popBackStack() }
            )
        }
        composable(
            route = ROUTE_SETTINGS_SCREEN
        ) {
            SettingsRoute(
                viewModel = viewModel,
                onTabClicked = { tabIndex -> navController.navigate(getRouteFromTabIndex(tabIndex)) }
            )
        }
    }
}

private fun getRouteFromTabIndex(selectedTabIndex: Int) : String {
    return when(selectedTabIndex) {
        TAB_TABLE -> ROUTE_TABLE_SCREEN
        TAB_POUCH -> ROUTE_POUCH_SCREEN
        TAB_SETTINGS -> ROUTE_SETTINGS_SCREEN
        else -> ROUTE_TABLE_SCREEN
    }
}

const val ROUTE_TABLE_SCREEN = "roll_screen_route"
const val ROUTE_POUCH_SCREEN = "pouch_screen_route"
const val ROUTE_DICE_SET_EDIT = "dice_set_edit_route"
const val ROUTE_SETTINGS_SCREEN = "settings_screen"