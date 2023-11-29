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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krystianrymonlipinski.dicepouch.ui.DicePouchTabRow
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.ChosenSetScreenState
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import com.krystianrymonlipinski.dicepouch.ui.TAB_TABLE
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.components.NoDiceCaption
import com.krystianrymonlipinski.dicepouch.ui.components.NoShortcutsCaption
import com.krystianrymonlipinski.dicepouch.ui.components.SecondaryCaption
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollDialog
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollSettingsDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import com.krystianrymonlipinski.dicepouch.viewmodels.MainActivityViewModel


@Composable
fun TableRoute(
    viewModel: MainActivityViewModel = hiltViewModel(),
    onTabClicked: (Int) -> Unit
) {
    val screenState by viewModel.tableScreenState.collectAsStateWithLifecycle()
    val rollingSettings by viewModel.rollingSettingStream.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.initiateCurrentSet()
    }

    TableScreen(
        screenState = screenState,
        rollingSettings = rollingSettings,
        onTabClicked = onTabClicked
    )
}

@Composable
fun TableScreen(
    screenState: ChosenSetScreenState = ChosenSetScreenState(
        chosenSet = DiceSet(dice = listOf(Die(4), Die(8)), shortcuts = listOf(RollShortcut(name = "Athletics check"))),
    ),
    rollingSettings: RollingSettings = RollingSettings(),
    onTabClicked: (Int) -> Unit = { }
) {
    var showRollSettingsDialog by rememberSaveable { mutableStateOf<Die?>(null) }
    var showRollDialog by rememberSaveable { mutableStateOf<RollSetting?>(null) }


    Scaffold { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            DicePouchTabRow(
                selectedTabIndex = TAB_TABLE,
                onTabClicked = { tabIndex -> onTabClicked(tabIndex) }
            )
            screenState.chosenSet?.let { chosenSet -> ChosenSetElementsLayout(
                chosenSet = chosenSet,
                onDieClicked = { die -> showRollSettingsDialog = die },
                onShortcutClicked = { shortcut -> showRollDialog = shortcut.setting }
            ) } ?: NoSetChosenCaption()

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
                    rollingSettings = rollingSettings,
                    onConfirmButtonClicked = { showRollDialog = null }
                )
            }
        }
    }
}

@Composable
fun ChosenSetElementsLayout(
    chosenSet: DiceSet,
    onDieClicked: (Die) -> Unit,
    onShortcutClicked: (RollShortcut) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ChosenSetName(name = chosenSet.info.name)
        Spacer(modifier = Modifier.height(24.dp))

        SecondaryCaption(text = stringResource(id = R.string.dice_caption))
        Spacer(modifier = Modifier.height(16.dp))

        if (chosenSet.dice.isEmpty()) NoDiceCaption()
        else DiceGrid(diceSet = chosenSet.dice, onDieClicked = onDieClicked)

        Spacer(modifier = Modifier.height(16.dp))
        SecondaryCaption(text = stringResource(id = R.string.shortcuts_caption))

        Spacer(modifier = Modifier.height(16.dp))
        if (chosenSet.shortcuts.isEmpty()) NoShortcutsCaption()
        else ShortcutsGrid(shortcutsSet = chosenSet.shortcuts, onShortcutClicked = onShortcutClicked)
    }
}

@Composable
fun NoSetChosenCaption() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_set_chosen),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun ChosenSetName(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
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
    }
}

@Composable
fun DiceGrid(
    diceSet: List<Die>,
    onDieClicked: (Die) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
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

@Composable
fun ShortcutsGrid(
    shortcutsSet: List<RollShortcut>,
    onShortcutClicked: (RollShortcut) -> Unit
) {
    LazyVerticalGrid( //TODO: improve container to make as wide as text
        columns = GridCells.Adaptive(80.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(shortcutsSet.size) { index ->
            ShortcutCard(shortcut = shortcutsSet[index], onClicked = onShortcutClicked)
        }
    }
}

@Composable
fun ShortcutCard(
    shortcut: RollShortcut,
    onClicked: (RollShortcut) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.clickable { onClicked(shortcut) },
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(containerColor = shortcut.setting.die.sideColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = shortcut.name,
            modifier = Modifier.padding(8.dp),
            color = shortcut.setting.die.numberColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall
        )
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun RollScreenPreview_WhenSetChosen() {
    DicePouchTheme {
        TableScreen()
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun RollScreenPreview_WhenNoSetChosen() {
    DicePouchTheme {
        TableScreen(screenState = ChosenSetScreenState(
            isLoadingCompleted = true,
            chosenSet = null
        ))
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun RollScreenPreview_WhenSetChosen_WithNoDiceAndShortcuts() {
    DicePouchTheme {
        TableScreen(screenState = ChosenSetScreenState(
            isLoadingCompleted = true,
            chosenSet = DiceSet(dice = emptyList(), shortcuts = emptyList())
        ))
    }
}