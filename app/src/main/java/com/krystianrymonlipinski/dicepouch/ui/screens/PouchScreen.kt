package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krystianrymonlipinski.dicepouch.ui.DicePouchTabRow
import com.krystianrymonlipinski.dicepouch.ui.DicePouchTopBar
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.PouchScreenState
import com.krystianrymonlipinski.dicepouch.room.AppDatabase
import com.krystianrymonlipinski.dicepouch.ui.TAB_POUCH
import com.krystianrymonlipinski.dicepouch.ui.dialogs.DiceSetConfigurationDialog
import com.krystianrymonlipinski.dicepouch.ui.dialogs.conditionalBorder
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import com.krystianrymonlipinski.dicepouch.viewmodels.MainActivityViewModel

@Composable
fun PouchRoute(
    viewModel: MainActivityViewModel = hiltViewModel(),
    onTabClicked: (Int) -> Unit,
    onBackStackPopped: () -> Unit,
    onEditSetClicked: (DiceSetInfo) -> Unit
) {
    val screenState by viewModel.pouchScreenState.collectAsStateWithLifecycle()

    PouchScreen(
        screenState = screenState,
        onTabClicked = onTabClicked,
        onBackStackPopped = onBackStackPopped,
        onNewSetAdded = { set -> viewModel.addNewSet(set.name, set.diceColor, set.numbersColor) },
        onChosenSetChanged = { set -> viewModel.changeChosenSet(set.id) },
        onEditSetClicked = onEditSetClicked,
        onSetDeleted = { set -> viewModel.deleteSet(set) }
    )
}

@Composable
fun PouchScreen(
    screenState: PouchScreenState = PouchScreenState(listOf(DiceSetInfo()), null),
    onBackStackPopped: () -> Unit = { },
    onTabClicked: (Int) -> Unit = { },
    onChosenSetChanged: (DiceSetInfo) -> Unit = { },
    onNewSetAdded: (DiceSetInfo) -> Unit = { },
    onEditSetClicked: (DiceSetInfo) -> Unit = { },
    onSetDeleted: (DiceSetInfo) -> Unit = { }
) {
    var setInEditMode by rememberSaveable { mutableStateOf<DiceSetInfo?>(null) }
    var shouldShowNewSetDialog by rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (setInEditMode != null) setInEditMode = null
        else onBackStackPopped()
    }

    Scaffold(
        topBar = { AnimatedVisibility(visible = setInEditMode != null) {
            DicePouchTopBar(
                title = setInEditMode?.name ?: "",
                navigationIcon = {
                    IconButton(onClick = { setInEditMode = null }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "arrow_back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onEditSetClicked(setInEditMode!!)
                        setInEditMode = null
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "edit_set",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        onSetDeleted(setInEditMode!!)
                        setInEditMode = null
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "delete_set",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }

        },
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()) {
            DicePouchTabRow(
                selectedTabIndex = TAB_POUCH,
                onTabClicked = { tabIndex -> onTabClicked(tabIndex) }
            )
            SetsGrid(
                screenState = screenState,
                setInEditMode = setInEditMode,
                onNewSetClicked = { shouldShowNewSetDialog = true },
                onSetClicked = onChosenSetChanged,
                onSetLongPressed = { longPressedSet -> setInEditMode = longPressedSet }
            )

            if (shouldShowNewSetDialog) {
                DiceSetConfigurationDialog(
                    onDialogDismissed = { shouldShowNewSetDialog = false },
                    onSetConfigurationConfirmed = { newSet ->
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
    screenState: PouchScreenState,
    setInEditMode: DiceSetInfo?,
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
        items(count = screenState.allSets.size) { index ->
            DiceSetGridElement(
                diceSetInfo = screenState.allSets[index],
                isCurrentSet = (screenState.currentlyChosenSetId ?: AppDatabase.DEFAULT_SET_ID)
                        == screenState.allSets[index].id,
                isHighlighted = setInEditMode?.let { it.id == screenState.allSets[index].id } ?: true,
                isClickable = setInEditMode == null,
                onSetClicked = { setClicked -> onSetClicked(setClicked) },
                onSetLongPressed = onSetLongPressed
            )
        }
        item { AddNewSetGridElement(
            onClicked = onNewSetClicked,
            isEnabled = setInEditMode == null
        ) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiceSetGridElement(
    diceSetInfo: DiceSetInfo,
    isCurrentSet: Boolean,
    isHighlighted: Boolean,
    isClickable: Boolean,
    onSetClicked: (DiceSetInfo) -> Unit,
    onSetLongPressed: (DiceSetInfo) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val alpha by animateFloatAsState(
        targetValue = if (isHighlighted) 1f else 0.38f,
        label = "alpha_animation"
    )

    ElevatedCard(
        modifier = Modifier
            .alpha(alpha)
            .aspectRatio(1f)
            .combinedClickable(
                enabled = isClickable,
                onClick = { onSetClicked(diceSetInfo) },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSetLongPressed(diceSetInfo)
                })
            .conditionalBorder(isCurrentSet) {
                this.border(
                    width = 4.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraLarge
                )
            },
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = diceSetInfo.diceColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
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
fun AddNewSetGridElement(
    onClicked: () -> Unit,
    isEnabled: Boolean
) {
    val alpha by animateFloatAsState(
        targetValue = if (isEnabled) 1f else 0.38f,
        label = "alpha_animation"
    )

    ElevatedCard(
        modifier = Modifier
            .alpha(alpha)
            .clickable(onClick = onClicked),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f),
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


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun PouchScreenPreview() {
    DicePouchTheme {
        PouchScreen()
    }
}