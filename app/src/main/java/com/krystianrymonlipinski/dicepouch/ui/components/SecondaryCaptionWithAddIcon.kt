package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krystianrymonlipinski.dicepouch.ui.components.icons.AddSetElementIcon

@Composable
fun SecondaryCaptionWithAddIcon(
    text: String,
    onIconClicked: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )
            AddSetElementIcon(onIconClicked = onIconClicked)
        }
        Divider(
            modifier = Modifier.height(2.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.secondary
        )
    }

}