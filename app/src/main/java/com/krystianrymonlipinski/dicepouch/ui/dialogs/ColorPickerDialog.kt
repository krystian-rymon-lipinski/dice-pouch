package com.krystianrymonlipinski.dicepouch.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.ui.components.CenteredDialogConfirmButton
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun ColorPickerDialog(
    initialColor: Color = Color.Magenta,
    onDialogDismissed: () -> Unit = { },
    onColorChanged: (Color) -> Unit = { }
) {
    val colorPickerController = rememberColorPickerController()

    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        onDismissRequest = onDialogDismissed,
        confirmButton = { CenteredDialogConfirmButton(
            text = stringResource(id = R.string.btn_dialog_save),
            onClick = { onColorChanged(colorPickerController.selectedColor.value) }
        ) },
        text = { ColorPickerDialogContent(
            initialColor = initialColor,
            colorPickerController = colorPickerController
        ) }
    )
}

@Composable
fun ColorPickerDialogContent(
    initialColor: Color,
    colorPickerController: ColorPickerController
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(1f),
            controller = colorPickerController,
            initialColor = initialColor
        )
        Spacer(modifier = Modifier.height(24.dp))
        AlphaSlider(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(8f),
            controller = colorPickerController,
            initialColor = initialColor,
            tileSize = 4.dp,
            tileEvenColor = Color.White,
            tileOddColor = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(8f),
            controller = colorPickerController,
            initialColor = initialColor
        )
        Spacer(modifier = Modifier.height(32.dp))
        ChosenColorPreview(colorPickerController.selectedColor.value)
    }
}

@Composable
fun ChosenColorPreview(selectedColor: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(0.25f).aspectRatio(1f),
        shape = MaterialTheme.shapes.extraSmall,
        color = selectedColor,
        content = { }
    )
}

@Preview
@Composable
fun ColorPickerDialogPreview() {
    DicePouchTheme {
        ColorPickerDialog()
    }
}