package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SecondaryCaption(text: String) {
    Column {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Divider(
            modifier = Modifier.height(2.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}