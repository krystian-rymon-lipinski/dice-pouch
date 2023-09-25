package com.krystianrymonlipinski.dicepouch.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.ui.components.CenteredDialogConfirmButton
import com.krystianrymonlipinski.dicepouch.ui.components.DieImage
import com.krystianrymonlipinski.dicepouch.ui.components.MinusIcon
import com.krystianrymonlipinski.dicepouch.ui.components.PlusIcon

@Composable
fun NewDieDialog(
    onDialogDismissed: () -> Unit,
    onNewDieAdded: (numberOfSides: Int) -> Unit
) {
    var currentSidesNumber by rememberSaveable { mutableStateOf(20) }

    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        onDismissRequest = onDialogDismissed,
        confirmButton = { CenteredDialogConfirmButton(
                text = stringResource(id = R.string.btn_add_new_die),
                onClick = { onNewDieAdded(currentSidesNumber) }
        ) },
        text = { NewDieDialogContent(
            currentSidesNumber = currentSidesNumber,
            onPlusMinusClicked = { change -> currentSidesNumber += change },
            onSeekbarValueChanged = { newValue -> currentSidesNumber = newValue }
        ) }
    )
}

@Composable
fun NewDieDialogContent(
    currentSidesNumber: Int,
    onPlusMinusClicked: (change: Int) -> Unit,
    onSeekbarValueChanged: (newValue: Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DieImage(die = Die(currentSidesNumber))
        Spacer(modifier = Modifier.padding(top = 16.dp))
        DieSidesControls(
            currentValue = currentSidesNumber,
            onPlusMinusClicked = onPlusMinusClicked
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        DieSidesSeekbar(
            currentValue = currentSidesNumber,
            onSeekbarValueChanged = onSeekbarValueChanged
        )
    }
}

@Composable
fun DieSidesControls(
    currentValue: Int,
    onPlusMinusClicked: (change: Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MinusIcon(
            onIconClicked = { onPlusMinusClicked(-1) },
            isEnabled = currentValue > MIN_DIE_SIDES
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(id = R.string.sides_caption)
        )
        PlusIcon(
            onIconClicked = { onPlusMinusClicked(1) },
            isEnabled = currentValue < MAX_DIE_SIDES
        )
    }

}

@Composable
fun DieSidesSeekbar(
    currentValue: Int,
    onSeekbarValueChanged: (newValue: Int) -> Unit
) {
    Slider(
        modifier = Modifier.semantics { contentDescription = "die_sides_slider" },
        valueRange = 3f.rangeTo(100f),
        value = currentValue.toFloat(),
        steps = 98,
        onValueChange = { newValue -> onSeekbarValueChanged(newValue.toInt()) }
    )
}

private const val MIN_DIE_SIDES = 3
private const val MAX_DIE_SIDES = 100