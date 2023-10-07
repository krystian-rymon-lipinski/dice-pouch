package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CenteredDialogConfirmButton(
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(
            onClick = onClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.secondary,
            ),
            enabled = isEnabled
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}