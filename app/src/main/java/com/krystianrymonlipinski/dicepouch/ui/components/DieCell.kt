package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun DieCell(
    modifier: Modifier = Modifier,
    die: Die = Die(6),
    valueShown: String? = die.sides.toString(),
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = getDieImage(die.sides)),
                contentDescription = "d${die.sides}",
                colorFilter = ColorFilter.tint(die.sideColor, blendMode = BlendMode.Modulate)
            )
            Text(
                text = valueShown ?: "",
                color = die.numberColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@DrawableRes
private fun getDieImage(numberOfSides: Int): Int {
    return when(numberOfSides) {
        4 -> R.drawable.d4
        6 -> R.drawable.d6
        8 -> R.drawable.d8
        10 -> R.drawable.d10
        12 -> R.drawable.d12
        20 -> R.drawable.d20
        else -> R.drawable.d6
    }
}

@Preview
@Composable
fun DieCellPreview() {
    DicePouchTheme {
        DieCell()
    }
}