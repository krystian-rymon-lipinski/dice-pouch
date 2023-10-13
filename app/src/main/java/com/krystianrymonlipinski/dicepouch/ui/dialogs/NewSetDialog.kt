package com.krystianrymonlipinski.dicepouch.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.ui.components.CenteredDialogConfirmButton
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun NewSetDialog(
    onDialogDismissed: () -> Unit = { },
    onNewSetAdded: (DiceSetInfo) -> Unit = { }
) {
    var currentSetState by rememberSaveable { mutableStateOf(DiceSetInfo(name = "New set")) }
    var showColorPicker by rememberSaveable { mutableStateOf<ColorType?>(null) }

    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        onDismissRequest = onDialogDismissed,
        confirmButton = { CenteredDialogConfirmButton(
            text = stringResource(id = R.string.btn_dialog_add),
            onClick = { onNewSetAdded(currentSetState) }
        ) },
        text = { NewSetDialogContent(
            setState = currentSetState,
            onNameChanged = { newValue -> currentSetState = currentSetState.changeName(newValue) },
            onColorPickerRequested = { colorType -> showColorPicker = colorType  }
        ) }
    )

    showColorPicker?.let { type ->
        ColorPickerDialog(
            initialColor = if (type == ColorType.DICE) currentSetState.diceColor else currentSetState.numbersColor,
            onColorChanged = { newColor ->
                currentSetState =
                    if (type == ColorType.DICE) currentSetState.changeDiceColor(newColor)
                    else currentSetState.changeNumbersColor(newColor)
            }
        )
    }
}

@Composable
fun NewSetDialogContent(
    setState: DiceSetInfo,
    onNameChanged: (String) -> Unit,
    onColorPickerRequested: (ColorType) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        NameInputField(currentName = setState.name, onNameChanged = onNameChanged)
        Spacer(modifier = Modifier.height(24.dp))
        RowWithColorPicker(
            caption = stringResource(id = R.string.dice_sides_color_colon),
            currentColor = setState.diceColor,
            onColorPickerRequested = { onColorPickerRequested(ColorType.DICE) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        RowWithColorPicker(
            caption = stringResource(id = R.string.dice_numbers_color_colon),
            currentColor = setState.numbersColor,
            onColorPickerRequested = { onColorPickerRequested(ColorType.NUMBERS) }
        )
    }
}

@Composable
fun RowWithColorPicker(
    caption: String,
    currentColor: Color,
    onColorPickerRequested: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f),
            text = caption
        )
        FilledTonalButton(
            onClick = onColorPickerRequested,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(32.dp),
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(containerColor = currentColor),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
            content = { }
        )
    }
}

enum class ColorType {
    DICE, NUMBERS
}


@Preview
@Composable
fun NewSetDialogContentPreview() {
    DicePouchTheme {
        NewSetDialog()
    }
}
