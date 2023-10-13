package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krystianrymonlipinski.dicepouch.DicePouchTabRow
import com.krystianrymonlipinski.dicepouch.DicePouchTopBar
import com.krystianrymonlipinski.dicepouch.MainActivityViewModel
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.ui.dialogs.NewSetDialog
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun PouchRoute(
    viewModel: MainActivityViewModel = hiltViewModel(),
    onTabClicked: (Int) -> Unit,
    onEditSetClicked: (DiceSetInfo) -> Unit
) {
    val screenState by viewModel.allSetsState.collectAsStateWithLifecycle()

    PouchScreen(
        allSetsState = screenState,
        onTabClicked = onTabClicked,
        onNewSetAdded = { set -> viewModel.addNewSet(set.name, set.diceColor, set.numbersColor) },
        onChosenSetChanged = { /*TODO: handle change in viewmodel */ },
        onEditSetClicked = onEditSetClicked,
        onSetDeleted = { set -> viewModel.deleteSet(set) }
    )
}

@Composable
fun PouchScreen(
    allSetsState: List<DiceSetInfo> = listOf(DiceSetInfo()),
    onTabClicked: (Int) -> Unit = { },
    onChosenSetChanged: (DiceSetInfo) -> Unit = { },
    onNewSetAdded: (DiceSetInfo) -> Unit = { },
    onEditSetClicked: (DiceSetInfo) -> Unit = { },
    onSetDeleted: (DiceSetInfo) -> Unit = { }
) {
    var setToBeEdited by rememberSaveable { mutableStateOf<DiceSetInfo?>(null) }
    var shouldShowNewSetDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.conditionalClickable(setToBeEdited != null) {
            this.clickable { setToBeEdited = null }
        },
        topBar = {
            DicePouchTopBar(
                title = stringResource(id = R.string.pouch_screen_top_bar_text),
                actions = {
                    AnimatedVisibility(visible = setToBeEdited != null) {
                        IconButton(onClick = {
                            onEditSetClicked(setToBeEdited!!)
                            setToBeEdited = null
                        }) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "edit_set")
                        }
                    }
                    AnimatedVisibility(visible = setToBeEdited != null) {
                        IconButton(onClick = {
                            onSetDeleted(setToBeEdited!!)
                            setToBeEdited = null
                        }) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete_set")
                        }
                    }
                }
            )
        },
        containerColor = if (setToBeEdited == null) Color.Transparent else Color.LightGray
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()) {
            DicePouchTabRow(
                selectedTabIndex = 1,
                onTabClicked = { tabIndex -> if (tabIndex != 1) onTabClicked(tabIndex) }
            )
            SetsGrid(
                sets = allSetsState,
                onNewSetClicked = { shouldShowNewSetDialog = true },
                onSetClicked = onChosenSetChanged,
                onSetLongPressed = { longPressedSet -> setToBeEdited = longPressedSet }
            )

            if (shouldShowNewSetDialog) {
                NewSetDialog(
                    onDialogDismissed = { shouldShowNewSetDialog = false },
                    onNewSetAdded = { newSet ->
                        onNewSetAdded(newSet)
                        shouldShowNewSetDialog = false
                    }
                )
            }
        }

    }
}

@Composable
fun SetsGrid(
    sets: List<DiceSetInfo>,
    onNewSetClicked: () -> Unit,
    onSetClicked: (DiceSetInfo) -> Unit,
    onSetLongPressed: (DiceSetInfo) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize(),
        columns = GridCells.Adaptive(120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = sets.size) { index ->
            DiceSetGridElement(
                diceSetInfo = sets[index],
                onSetClicked = onSetClicked,
                onSetLongPressed = onSetLongPressed
            )
        }
        item { AddNewSetGridElement(onClicked = onNewSetClicked) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiceSetGridElement(
    diceSetInfo: DiceSetInfo,
    onSetClicked: (DiceSetInfo) -> Unit,
    onSetLongPressed: (DiceSetInfo) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    ElevatedCard(
        Modifier.combinedClickable(
            onClick = { onSetClicked(diceSetInfo) },
            onLongClick = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onSetLongPressed(diceSetInfo)
            }
        ),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = diceSetInfo.diceColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = diceSetInfo.name,
                color = diceSetInfo.numbersColor,
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}

@Composable
fun AddNewSetGridElement(onClicked: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.clickable { onClicked() },
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.plus_sign),
                color = Color.Black,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
fun Modifier.conditionalClickable(condition: Boolean, modifier: @Composable Modifier.() -> Modifier) =
    then(if (condition) modifier.invoke(this) else this)



@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun PouchScreenPreview() {
    DicePouchTheme {
        PouchScreen()
    }
}