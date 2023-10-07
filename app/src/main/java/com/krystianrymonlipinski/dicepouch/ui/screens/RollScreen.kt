package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krystianrymonlipinski.dicepouch.MainActivityViewModel
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollDialog
import com.krystianrymonlipinski.dicepouch.ui.dialogs.RollSettingsDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme


@Composable
fun RollRoute(
    viewModel: MainActivityViewModel = hiltViewModel(),
    onEditIconClicked: () -> Unit = { }
) {
    val screenState by viewModel.diceSetState.collectAsStateWithLifecycle()
    RollScreen(
        screenState = screenState,
        onEditIconClicked = onEditIconClicked
    )
}

@Composable
fun RollScreen(
    screenState: DiceSet = DiceSet(dice = listOf(Die(4), Die(8)), shortcuts = listOf(RollShortcut(name = "Athletics check"))),
    onEditIconClicked: () -> Unit = { },
) {
    var showRollSettingsDialog by rememberSaveable { mutableStateOf<Die?>(null) }
    var showRollDialog by rememberSaveable { mutableStateOf<RollSetting?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {
        ChosenSetName(name = screenState.name, onEditIconClicked = onEditIconClicked)
        DiceText()
        Spacer(modifier = Modifier.height(8.dp))
        DiceGrid(
            diceSet = screenState.dice,
            onDieClicked = { die -> showRollSettingsDialog = die }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ShortcutsText()
        Spacer(modifier = Modifier.height(8.dp))
        ShortcutsGrid(
            shortcutsSet = screenState.shortcuts,
            onShortcutClicked = { shortcut -> showRollDialog = shortcut.setting }
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
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
fun DiceText() {
    Text(
        text = stringResource(id = R.string.dice_caption),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        )
    )
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
fun ShortcutsText() {
    Text(
        text = stringResource(id = R.string.shortcuts_caption),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        )
    )
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
fun RollScreenPreview() {
    DicePouchTheme {
        RollScreen()
    }
}