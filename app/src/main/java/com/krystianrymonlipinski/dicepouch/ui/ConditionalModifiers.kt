package com.krystianrymonlipinski.dicepouch.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

fun Modifier.conditionalBackground(
    condition: Boolean,
    color: Color
) =
    if (condition) then(background(color = color))
    else this


fun Modifier.conditionalBorder(
    condition: Boolean,
    width: Dp,
    color: Color,
    shape: Shape
) =
    if (condition) then(border(
        width = width,
        color = color,
        shape = shape
    ))
    else this