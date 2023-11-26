package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.krystianrymonlipinski.dicepouch.R

@Composable
fun NoShortcutsCaption() {
    Text(
        text = stringResource(id = R.string.no_shortcuts_added),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge
            .copy(fontWeight = FontWeight.Bold),
        textAlign = TextAlign.Center
    )
}