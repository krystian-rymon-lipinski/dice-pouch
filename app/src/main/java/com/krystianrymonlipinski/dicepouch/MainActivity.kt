package com.krystianrymonlipinski.dicepouch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<MainActivityViewModel>()

            DicePouchTheme {
                AppNavHost(navController = rememberNavController(), viewModel = viewModel)
            }
        }
    }
}