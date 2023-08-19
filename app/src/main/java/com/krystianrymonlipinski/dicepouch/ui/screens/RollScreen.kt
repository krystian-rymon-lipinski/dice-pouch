package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun RollScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DiceGrid(basicDndDiceSet)
    }
}

@Composable
fun DiceGrid(diceSet: List<Die>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(80.dp),
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Text(
                text = basicDndSetName,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(
            count = diceSet.size,
        ) {
            DieCell(diceSet[it])
        }
    }
}

@Composable
fun DieCell(die: Die) {
    Surface(
        shape = RectangleShape,
        color = die.sideColor,
        shadowElevation = 4.dp
    ) {
        Text(
            text = die.sides.toString(),
            color = die.numberColor,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

private val basicDndSetName = "Basic D&D Set"

private val basicDndDiceSet = listOf(
    Die(4, Color.White, Color.Black),
    Die(6, Color.Green, Color.Red),
    Die(8, Color.White, Color.Black),
    Die(10, Color.White, Color.Black),
    Die(10, Color.White, Color.Black),
    Die(12, Color.White, Color.Black),
    Die(20, Color.White, Color.Black)
)


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun RollScreenPreview() {
    DicePouchTheme {
        RollScreen()
    }
}