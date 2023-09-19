package com.krystianrymonlipinski.dicepouch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.krystianrymonlipinski.dicepouch.ui.screens.RollScreen
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DicePouchTheme {
                RollScreen()
            }
        }
    }
}