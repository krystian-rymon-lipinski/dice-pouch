package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.krystianrymonlipinski.dicepouch.model.Die

@Composable
fun DieCell(
    die: Die,
    valueShown: String? = die.sides.toString(),
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        color = die.sideColor,
        shadowElevation = 4.dp
    ) {
        Text(
            text = valueShown ?: "",
            color = die.numberColor,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}