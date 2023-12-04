package com.krystianrymonlipinski.dicepouch.ui.components.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun AddSetElementIcon(onIconClicked: () -> Unit) {
    IconButton(onClick = onIconClicked) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "add_set_element_icon",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun DeleteElementIcon(
    modifier: Modifier = Modifier,
    iconColor: Color,
    onIconClicked: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onIconClicked
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "delete_set_element_icon",
            tint = iconColor
        )
    }
}