package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.krystianrymonlipinski.dicepouch.model.Die

@Composable
fun DieCell(
    die: Die,
    onDieClicked: (Die) -> Unit,
    currentValue: Int = die.sides
) {
    Surface(
        modifier = Modifier.clickable { onDieClicked(die) },
        shape = RectangleShape,
        color = die.sideColor,
        shadowElevation = 4.dp
    ) {
        Text(
            text = currentValue.toString(),
            color = die.numberColor,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}