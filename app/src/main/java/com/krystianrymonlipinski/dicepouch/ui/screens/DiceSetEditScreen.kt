package com.krystianrymonlipinski.dicepouch.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krystianrymonlipinski.dicepouch.MainActivityViewModel
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.dialogs.NewDieDialog


@Composable
fun DiceSetEditRoute(
    viewModel: MainActivityViewModel = hiltViewModel(),
) {
    val screenState by viewModel.diceSetState.collectAsStateWithLifecycle()

    DiceSetEditScreen(
        screenState = screenState,
        onNewDieAdded = { numberOfSides -> viewModel.addNewDieToSet(numberOfSides) },
        onDeleteDieClicked = { die -> viewModel.deleteDieFromSet(die) }
    )
}

@Composable
fun DiceSetEditScreen(
    screenState: DiceSet = DiceSet(),
    onNewDieAdded: (Int) -> Unit = {},
    onDeleteDieClicked: (Die) -> Unit = {}
) {
    var showNewDieDialog by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        DiceCaption(onAddNewDieClicked = { showNewDieDialog = true })
        EditableDiceGrid(
            diceSet = screenState.dice,
            onDeleteDieClicked = onDeleteDieClicked
        )
    }

    if (showNewDieDialog) {
        NewDieDialog(
            onDialogDismissed = { showNewDieDialog = false },
            onNewDieAdded = { numberOfSides ->
                showNewDieDialog = false
                onNewDieAdded(numberOfSides)
            }
        )
    }
}

@Composable
fun EditableDiceGrid(
    diceSet: List<Die>,
    onDeleteDieClicked: (Die) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(diceSet.size) {dieIndex: Int ->
            DeletableDieImage(
                die = diceSet[dieIndex],
                onDeleteDieClicked = onDeleteDieClicked
            )
        }
    }
}

@Composable
fun DeletableDieImage(die: Die, onDeleteDieClicked: (Die) -> Unit) {
    ElevatedCard(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DieImage(
                die = die,
                textStyle = MaterialTheme.typography.headlineMedium
            )
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { onDeleteDieClicked(die) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "delete_die_icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun DiceCaption(onAddNewDieClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.dice_caption),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(onClick = onAddNewDieClicked) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "add_die_icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

