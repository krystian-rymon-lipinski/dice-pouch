package com.krystianrymonlipinski.dicepouch.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.ui.components.CenteredDialogConfirmButton

@Composable
fun SetNameChangeDialog(
    currentSetName: String,
    onDialogDismissed: () -> Unit,
    onSetNameChanged: (String) -> Unit
) {
    var setName by rememberSaveable { mutableStateOf(currentSetName) }

    AlertDialog(
        onDismissRequest = onDialogDismissed,
        confirmButton = { CenteredDialogConfirmButton(
            text = stringResource(id = R.string.btn_dialog_save),
            onClick = { onSetNameChanged(setName) },
            isEnabled = setName.isNotBlank()
        ) },
        text = { SetNameChangeDialogContent(
            setName = setName,
            onSetNameChanged = { newName -> setName = newName }
        ) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun SetNameChangeDialogContent(
    setName: String,
    onSetNameChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = setName,
        onValueChange = onSetNameChanged,
        placeholder = { Text(text = stringResource(id = R.string.placeholder_set_name)) },
        isError = setName.isNotBlank(),
    )
}