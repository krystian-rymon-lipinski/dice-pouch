package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krystianrymonlipinski.dicepouch.DicePouchTopBar
import com.krystianrymonlipinski.dicepouch.MainActivityViewModel
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.dialogs.NewDieDialog
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollShortcutDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import kotlinx.coroutines.launch


@Composable
fun DiceSetEditRoute(
    viewModel: MainActivityViewModel = hiltViewModel(),
    onUpClicked: () -> Unit
) {
    val screenState by viewModel.diceSetState.collectAsStateWithLifecycle()

    DiceSetEditScreen(
        screenState = screenState,
        onUpClicked = onUpClicked,
        onNewDieAdded = { numberOfSides -> viewModel.addNewDieToSet(numberOfSides) },
        onDeleteDieClicked = { die -> viewModel.deleteDieFromSet(die) },
        onNewShortcutAdded = { name, setting -> viewModel.addNewShortcutToSet(name, setting) },
        onShortcutUpdated = { shortcut -> viewModel.updateShortcut(shortcut) },
        onDeleteShortcutClicked = { shortcut -> viewModel.deleteShortcut(shortcut) }
    )
}

@Composable
fun DiceSetEditScreen(
    screenState: DiceSet = DiceSet(DiceSetInfo(0, "A set"), listOf(Die(20), Die(15)), listOf(RollShortcut(name = "Some check"))),
    onUpClicked: () -> Unit = { },
    onNewDieAdded: (Int) -> Unit = {},
    onDeleteDieClicked: (Die) -> Unit = {},
    onNewShortcutAdded: (String, RollSetting) -> Unit = { _, _ -> /* Nothing by default */},
    onShortcutUpdated: (RollShortcut) -> Unit = {},
    onDeleteShortcutClicked: (RollShortcut) -> Unit = {}
) {
    var showNewDieDialog by rememberSaveable { mutableStateOf(false) }
    var showNewShortcutDialog by rememberSaveable { mutableStateOf(false) }
    var showUpdateShortcutDialog by rememberSaveable { mutableStateOf<RollShortcut?>(null) }
    var showDeleteDieDialog by rememberSaveable { mutableStateOf<Die?>(null) }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage = stringResource(id = R.string.no_dice_snackbar_message)

    Scaffold(
        topBar = {
            DicePouchTopBar(
                title = stringResource(id = R.string.edit_set_screen_top_bar_text),
                navigationIcon = { IconButton(onClick = onUpClicked ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "arrow_back")
                } }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxWidth()
        ) {
            DiceCaption(onAddNewDieClicked = { showNewDieDialog = true })
            Spacer(modifier = Modifier.height(8.dp))
            EditableDiceGrid(
                diceSet = screenState.dice,
                onDeleteDieClicked = { dieToBeDeleted ->
                    if (screenState.shortcuts.any { it.setting.die == dieToBeDeleted }) showDeleteDieDialog = dieToBeDeleted
                    else onDeleteDieClicked(dieToBeDeleted)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            ShortcutsCaption(onAddShortcutClicked = {
                if (screenState.dice.isNotEmpty()) showNewShortcutDialog = true
                else scope.launch { snackBarHostState.showSnackbar(
                    message = snackBarMessage,
                    duration = SnackbarDuration.Short
                ) }
            })
            Spacer(modifier = Modifier.height(8.dp))
            EditableShortcutsGrid(
                screenState.shortcuts,
                onShortcutClicked = { shortcut -> showUpdateShortcutDialog = shortcut },
                onDeleteShortcutClicked = onDeleteShortcutClicked
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

        showDeleteDieDialog?.let { dieToBeDeleted ->
            DeleteDieAlertDialog(
                onDialogDismissed = { showDeleteDieDialog = null },
                onDeleteButtonClicked = {
                    onDeleteDieClicked(dieToBeDeleted)
                    showDeleteDieDialog = null
                }
            )
        }

        if (showNewShortcutDialog || showUpdateShortcutDialog != null) {
            RollShortcutDialog(
                shortcut = showUpdateShortcutDialog,
                diceInSet = screenState.dice,
                onDialogDismissed = {
                    showNewShortcutDialog = false
                    showUpdateShortcutDialog = null
                },
                onSaveShortcutClicked = { shortcut ->
                    if (showNewShortcutDialog) onNewShortcutAdded(shortcut.name, shortcut.setting)
                    else onShortcutUpdated(shortcut)

                    showNewShortcutDialog = false
                    showUpdateShortcutDialog = null
                }
            )
        }
    }
}

@Composable
fun DeleteDieAlertDialog(
    onDialogDismissed: () -> Unit,
    onDeleteButtonClicked: () -> Unit
) {
    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        onDismissRequest = onDialogDismissed,
        confirmButton = {
            OutlinedButton(onClick = onDeleteButtonClicked) {
                Text(text = stringResource(id = R.string.btn_yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDialogDismissed) {
                Text(text = stringResource(id = R.string.btn_no))
            }
        },
        text = { Text(text = stringResource(id = R.string.delete_die_dialog_message)) }
    )
}

@Composable
fun DiceCaption(onAddNewDieClicked: () -> Unit) {
    RowWithCaptionAndPlusIcon(
        captionText = stringResource(id = R.string.dice_caption),
        onPlusIconClicked = onAddNewDieClicked
    )
}

@Composable
fun EditableDiceGrid(
    diceSet: List<Die>,
    onDeleteDieClicked: (Die) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
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
fun ShortcutsCaption(onAddShortcutClicked: () -> Unit) {
    RowWithCaptionAndPlusIcon(
        captionText = stringResource(id = R.string.shortcuts_caption),
        onPlusIconClicked = onAddShortcutClicked
    )
}

@Composable
fun EditableShortcutsGrid(
    shortcuts: List<RollShortcut>,
    onShortcutClicked: (RollShortcut) -> Unit,
    onDeleteShortcutClicked: (RollShortcut) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(80.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(shortcuts.size) { index ->
            DeletableShortcutCard(
                shortcut = shortcuts[index],
                onShortcutClicked = onShortcutClicked,
                onDeleteShortcutClicked = onDeleteShortcutClicked
            )
        }
    }
}

@Composable
fun DeletableShortcutCard(
    shortcut: RollShortcut,
    onShortcutClicked: (RollShortcut) -> Unit,
    onDeleteShortcutClicked: (RollShortcut) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.clickable { onShortcutClicked(shortcut) },
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(containerColor = shortcut.setting.die.sideColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = shortcut.name,
                modifier = Modifier.weight(1f),
                color = shortcut.setting.die.numberColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall
            )
            IconButton(onClick = { onDeleteShortcutClicked(shortcut) }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "delete_shortcut",
                    tint = shortcut.setting.die.numberColor
                )
            }
        }
    }
}

@Composable
fun RowWithCaptionAndPlusIcon(
    captionText: String,
    onPlusIconClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = captionText,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(onClick = onPlusIconClicked) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "add_icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 640)
fun DiceSetEditScreenPreview() {
    DicePouchTheme {
        DiceSetEditScreen()
    }
}

