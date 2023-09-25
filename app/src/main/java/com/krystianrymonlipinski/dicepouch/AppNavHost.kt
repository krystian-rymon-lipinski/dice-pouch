package com.krystianrymonlipinski.dicepouch

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.krystianrymonlipinski.dicepouch.ui.screens.DiceSetEditScreen
import com.krystianrymonlipinski.dicepouch.ui.screens.RollScreen

@Composable
fun AppNavHost(navController: NavHostController, viewModel: MainActivityViewModel) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_ROLL_SCREEN,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = ROUTE_ROLL_SCREEN) {
            val screenState by viewModel.diceSetState.collectAsStateWithLifecycle()
            RollScreen(
                screenState = screenState,
                onEditIconClicked = { navController.navigate(ROUTE_DICE_SET_EDIT) }
            )
        }
        composable(route = ROUTE_DICE_SET_EDIT) {
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