package com.krystianrymonlipinski.dicepouch.model

import androidx.compose.ui.graphics.Color

data class Die(
    val sides: Int,
    val sideColor: Color = Color.White,
    val numberColor: Color = Color.Black
)