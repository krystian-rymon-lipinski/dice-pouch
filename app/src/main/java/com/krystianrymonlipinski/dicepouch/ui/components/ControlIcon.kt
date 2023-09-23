package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ControlIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onIconClicked: () -> Unit,
) {
    IconButton(
        onClick = { onIconClicked() },
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small)
    ) { Icon(imageVector = imageVector, contentDescription = contentDescription) }
}