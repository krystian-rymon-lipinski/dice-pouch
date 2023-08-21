package com.krystianrymonlipinski.dicepouch.ui.dialogs

import RollDescription
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme

@Composable
fun RollDialog(
    setting: RollSetting = RollSetting(Die(6)),
    onConfirmButtonClicked: () -> Unit = {},
) {

    AlertDialog(
        onDismissRequest = { /* Current properties do not allow this */ },
        confirmButton = { ConfirmButton(
            onConfirmButtonClicked = onConfirmButtonClicked
        ) },
        text = { RollDialogContent(setting) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}

@Composable
fun RollDialogContent(setting: RollSetting) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RollDescription(
                description = setting.rollDescription,
                textStyle = MaterialTheme.typography.titleSmall
            )
            RollResult()
            ResultParts()
        }
}

@Composable
fun RollResult() {
    Text(
        text = "11",
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        style = MaterialTheme.typography.displayLarge
    )
}

@Composable
fun ResultParts() {
    Text(text = "4 + 4 + 3")
}

@Composable
fun ConfirmButton(onConfirmButtonClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(onClick = { onConfirmButtonClicked() }) {
            Text(
                text = stringResource(id = R.string.btn_ok),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}



@Composable
@Preview
fun RollDialogPreview() {
    DicePouchTheme {
        RollDialog()
    }
}