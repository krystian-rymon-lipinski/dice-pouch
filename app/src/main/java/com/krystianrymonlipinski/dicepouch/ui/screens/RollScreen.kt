package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollDialog
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollSettingsDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun RollScreen(
    screenState: DiceSet = DiceSet(),
    onEditIconClicked: () -> Unit = { }
) {
    var showRollSettingsDialog by rememberSaveable { mutableStateOf<Die?>(null) }
    var showRollDialog by rememberSaveable { mutableStateOf<RollSetting?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        ChosenSetName(name = screenState.name, onEditIconClicked = onEditIconClicked)
        DiceGrid(
            diceSet = screenState.dice,
            onDieClicked = { die -> showRollSettingsDialog = die }
        )
    }

    showRollSettingsDialog?.let {
        RollSettingsDialog(
            it,
            onDismissDialog = { showRollSettingsDialog = null },
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

@Composable
fun ChosenSetName(
    name: String,
    onEditIconClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(onClick = onEditIconClicked) {
            Image( //TODO: improve all icon buttons; for their selected state is not highlighted; also use icons, not images
                imageVector = Icons.Filled.Edit,
                contentDescription = "edit_set_icon",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
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
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = diceSet.size) { die ->
            ElevatedCard(
                modifier = Modifier.clickable { onDieClicked(diceSet[die]) },
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    DieImage(
                        die = diceSet[die],
                        textStyle = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun RollScreenPreview() {
    DicePouchTheme {
        RollScreen()
    }
}