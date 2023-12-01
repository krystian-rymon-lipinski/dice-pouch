package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krystianrymonlipinski.dicepouch.ui.DicePouchTopBar
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.components.LoadingScreen
import com.krystianrymonlipinski.dicepouch.ui.components.NoDiceCaption
import com.krystianrymonlipinski.dicepouch.ui.components.NoShortcutsCaption
import com.krystianrymonlipinski.dicepouch.ui.components.SecondaryCaptionWithIcon
import com.krystianrymonlipinski.dicepouch.ui.dialogs.DiceSetConfigurationDialog
import com.krystianrymonlipinski.dicepouch.ui.dialogs.NewDieDialog
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollShortcutDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import com.krystianrymonlipinski.dicepouch.viewmodels.MainActivityViewModel
import kotlinx.coroutines.launch


@Composable
fun DiceSetEditRoute(
    viewModel: MainActivityViewModel = hiltViewModel(),
    chosenSetId: Int,
    onUpClicked: () -> Unit
) {

    val setBeingEdited by viewModel.retrieveSetWithChosenId(chosenSetId).collectAsStateWithLifecycle(
        initialValue = null
    )

    DiceSetEditScreen(
        setBeingEdited = setBeingEdited,
        onUpClicked = onUpClicked,
        onSetInfoChanged = { newInfo -> viewModel.changeSetInfo(newInfo) },
        onNewDieAdded = { numberOfSides -> viewModel.addNewDieToSet(chosenSetId, numberOfSides) },
        onDeleteDieClicked = { die -> viewModel.deleteDieFromSet(chosenSetId, die) },
        onNewShortcutAdded = { name, setting -> viewModel.addNewShortcutToSet(name, setting) },
        onShortcutUpdated = { shortcut -> viewModel.updateShortcut(shortcut) },
        onDeleteShortcutClicked = { shortcut -> viewModel.deleteShortcut(shortcut) }
    )
}

@Composable
fun DiceSetEditScreen(
    setBeingEdited: DiceSet? = DiceSet(DiceSetInfo(0, "A set"), listOf(Die(20), Die(15)), listOf(RollShortcut(name = "Some check"))),
    onUpClicked: () -> Unit = { },
    onSetInfoChanged: (DiceSetInfo) -> Unit = { },
    onNewDieAdded: (Int) -> Unit = {},
    onDeleteDieClicked: (Die) -> Unit = {},
    onNewShortcutAdded: (String, RollSetting) -> Unit = { _, _ -> /* Nothing by default */},
    onShortcutUpdated: (RollShortcut) -> Unit = {},
    onDeleteShortcutClicked: (RollShortcut) -> Unit = {}
) {

    var showSetNameChangeDialog by rememberSaveable { mutableStateOf(false) }
    var showNewDieDialog by rememberSaveable { mutableStateOf(false) }
    var showNewShortcutDialog by rememberSaveable { mutableStateOf(false) }
    var showUpdateShortcutDialog by rememberSaveable { mutableStateOf<RollShortcut?>(null) }
    var showDeleteDieDialog by rememberSaveable { mutableStateOf<Die?>(null) }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage = stringResource(id = R.string.no_dice_snackbar_message)

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = { DicePouchTopBar(
            title = setBeingEdited?.info?.name ?: "",
            navigationIcon = { IconButton(onClick = onUpClicked ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "arrow_back",
                    tint = MaterialTheme.colorScheme.primary
                )
            } },
            actions = { IconButton(onClick = { showSetNameChangeDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "primary_caption_icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            } }
        ) }
    ) { paddingValues ->
        setBeingEdited?.let { diceSet ->
            ChosenSetElementsLayout(
                paddingValues = paddingValues,
                chosenSet = diceSet,
                onAddNewDieClicked = { showNewDieDialog = true },
                onDeleteDieClicked = { dieToBeDeleted ->
                    if (diceSet.shortcuts.any { it.setting.die == dieToBeDeleted }) showDeleteDieDialog = dieToBeDeleted
                    else onDeleteDieClicked(dieToBeDeleted) },
                onAddShortcutClicked = {
                    if (diceSet.dice.isNotEmpty()) showNewShortcutDialog = true
                    else scope.launch {
                        if (snackBarHostState.currentSnackbarData == null) {
                            snackBarHostState.showSnackbar(
                                message = snackBarMessage,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                onShortcutClicked = { shortcutClicked -> showUpdateShortcutDialog = shortcutClicked},
                onDeleteShortcutClicked = onDeleteShortcutClicked
            )
        } ?: LoadingScreen(modifier = Modifier.fillMaxSize())

        if (showSetNameChangeDialog) {
            DiceSetConfigurationDialog(
                currentConfiguration = setBeingEdited?.info,
                onDialogDismissed = { showSetNameChangeDialog = false },
                onSetConfigurationConfirmed = { setInfo ->
                    onSetInfoChanged(setInfo)
                    showSetNameChangeDialog = false
                }
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
                diceInSet = setBeingEdited?.dice ?: emptyList(),
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
fun ChosenSetElementsLayout(
    paddingValues: PaddingValues,
    chosenSet: DiceSet,
    onAddNewDieClicked: () -> Unit,
    onDeleteDieClicked: (Die) -> Unit,
    onAddShortcutClicked: () -> Unit,
    onShortcutClicked: (RollShortcut) -> Unit,
    onDeleteShortcutClicked: (RollShortcut) -> Unit
) {
    Column(modifier = Modifier
        .padding(
            top = paddingValues.calculateTopPadding(),
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        )
        .fillMaxWidth()
    ) {
        SecondaryCaptionWithIcon(
            text = stringResource(id = R.string.dice_caption),
            imageVector = Icons.Filled.Add,
            onIconClicked = onAddNewDieClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditableDiceGrid(
            modifier = Modifier.weight(1f),
            diceSet = chosenSet.dice,
            onDeleteDieClicked = onDeleteDieClicked
        )

        Spacer(modifier = Modifier.height(16.dp))
        SecondaryCaptionWithIcon(
            text = stringResource(id = R.string.shortcuts_caption),
            imageVector = Icons.Filled.Add,
            onIconClicked = onAddShortcutClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditableShortcutsGrid(
            modifier = Modifier.weight(1f),
            shortcuts = chosenSet.shortcuts,
            onShortcutClicked = onShortcutClicked,
            onDeleteShortcutClicked = onDeleteShortcutClicked
        )
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
fun EditableDiceGrid(
    modifier: Modifier,
    diceSet: List<Die>,
    onDeleteDieClicked: (Die) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(130.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (diceSet.isEmpty()) {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                NoDiceCaption()
            }
        } else {
            items(diceSet.size) {dieIndex: Int ->
                DeletableDieImage(
                    die = diceSet[dieIndex],
                    onDeleteDieClicked = onDeleteDieClicked
                )
            }
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
            DieImage(die = die)
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { onDeleteDieClicked(die) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "delete_die_icon",
                    tint = die.numberColor
                )
            }
        }
    }
}

@Composable
fun EditableShortcutsGrid(
    modifier: Modifier,
    shortcuts: List<RollShortcut>,
    onShortcutClicked: (RollShortcut) -> Unit,
    onDeleteShortcutClicked: (RollShortcut) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(3),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (shortcuts.isEmpty()) {
            item(StaggeredGridItemSpan.FullLine) {
                NoShortcutsCaption()
            }
        } else {
            items(shortcuts.size) { index ->
                DeletableShortcutCard(
                    shortcut = shortcuts[index],
                    onShortcutClicked = onShortcutClicked,
                    onDeleteShortcutClicked = onDeleteShortcutClicked
                )
            }
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
                .padding(start = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = shortcut.name,
                modifier = Modifier.weight(1f),
                color = shortcut.setting.die.numberColor,
                textAlign = TextAlign.Center,
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
@Preview(showBackground = true, widthDp = 320, heightDp = 640)
fun DiceSetEditScreenPreview() {
    DicePouchTheme {
        DiceSetEditScreen()
    }
}

