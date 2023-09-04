package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollDialog
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollSettingsDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun RollScreen() {
    var showRollSettingsDialog by rememberSaveable { mutableStateOf<Die?>(null) }
    var showRollDialog by rememberSaveable { mutableStateOf<RollSetting?>(null) }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        DiceGrid(
            diceSet = basicDndDiceSet,
            onDieClicked = { die -> showRollSettingsDialog = die }
        )
        showRollSettingsDialog?.let {
            RollSettingsDialog(
                it,
                onDismissRequest = { showRollSettingsDialog = null },
                onRollButtonClicked = { rollSettings ->
                    showRollSettingsDialog = null
                    showRollDialog = rollSettings
                }
            )
        }
        showRollDialog?.let {
            RollDialog(
                setting = it,
                onConfirmButtonClicked = { showRollDialog = null }
            )
        }
    }
}

@Composable
fun DiceGrid(
    diceSet: List<Die>,
    onDieClicked: (Die) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Text(
                text = BASIC_DND_SET_NAME,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(count = diceSet.size) { die ->
            Surface(
                modifier = Modifier.clickable { onDieClicked(diceSet[die]) },
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shadowElevation = 4.dp
            ) {
                DieImage(
                    die = diceSet[die],
                    textStyle = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}


private const val BASIC_DND_SET_NAME = "Basic D&D Set"

private val basicDndDiceSet = listOf(
    Die(4),
    Die(6, Color.Green, Color.Red),
    Die(8),
    Die(10),
    Die(10),
    Die(12),
    Die(20)
)


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun RollScreenPreview() {
    DicePouchTheme {
        RollScreen()
    }
}