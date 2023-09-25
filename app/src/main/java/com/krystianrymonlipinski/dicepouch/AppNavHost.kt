package com.krystianrymonlipinski.dicepouch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            val viewModel = hiltViewModel<MainActivityViewModel>()
            val screenState by viewModel.diceSetState.collectAsStateWithLifecycle()
            RollScreen(
                screenState = screenState,
                onEditIconClicked = { navController.navigate(ROUTE_DICE_SET_EDIT) }
            )
        }
        composable(route = ROUTE_DICE_SET_EDIT) {
            val viewModel = hiltViewModel<MainActivityViewModel>()
            val screenState by viewModel.diceSetState.collectAsStateWithLifecycle()
            DiceSetEditScreen(
                screenState = screenState,
                onNewDieAdded = { numberOfSides -> viewModel.addNewDieToSet(numberOfSides) },
                onDieDeleted = { index -> viewModel.deleteDieFromSet(index) }
            )
        }
    }
}

const val ROUTE_ROLL_SCREEN = "roll_screen_route"
const val ROUTE_DICE_SET_EDIT = "dice_set_edit_route"