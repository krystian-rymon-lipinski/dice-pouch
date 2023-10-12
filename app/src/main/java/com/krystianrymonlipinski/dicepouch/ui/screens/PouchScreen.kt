package com.krystianrymonlipinski.dicepouch.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun PouchScreen(
    allSetsState: List<DiceSetInfo> = listOf(DiceSetInfo()),
    onChosenSetChanged: (DiceSetInfo) -> Unit = { }, /*TODO: pass info to viewmodel */
    onNewSetAdded: (DiceSetInfo) -> Unit = { },
    onSetDeleted: (DiceSetInfo) -> Unit = { }
) {
    var setToBeEdited by rememberSaveable { mutableStateOf<DiceSetInfo?>(null) }
    var shouldShowNewSetDialog by rememberSaveable { mutableStateOf(false) }

    SetsGrid(
        sets = allSetsState,
        onNewSetClicked = { shouldShowNewSetDialog = true },
        onSetClicked = onChosenSetChanged,
        onSetLongPressed = { longPressedSet -> setToBeEdited = longPressedSet }
    )

    setToBeEdited?.let {
        /*TODO: show context icons; gray out main layout; exit edit mode upon clicking on main layout  */
    }

    if (shouldShowNewSetDialog) {
        //TODO: define NewDiceSetDialog
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



@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun PouchScreenPreview() {
    DicePouchTheme {
        PouchScreen()
    }
}