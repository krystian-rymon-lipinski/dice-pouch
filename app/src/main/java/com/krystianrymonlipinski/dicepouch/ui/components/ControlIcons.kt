package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
    isEnabled: Boolean = true
) {
    IconButton(
        onClick = { onIconClicked() },
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small),
        enabled = isEnabled
    ) { Icon(imageVector = imageVector, contentDescription = contentDescription) }
}

@Composable
fun PlusIcon(
    onIconClicked: () -> Unit,
    isEnabled: Boolean = true
) {
    ControlIcon(
        imageVector = Icons.Filled.Add,
        contentDescription = "plus",
        onIconClicked = onIconClicked,
        isEnabled = isEnabled
    )
}

@Composable
fun MinusIcon(
    onIconClicked: () -> Unit,
    isEnabled: Boolean = true
) {
    ControlIcon(
        imageVector = Icons.Filled.Remove,
        contentDescription = "minus",
        onIconClicked = onIconClicked,
        isEnabled = isEnabled
    )
}