package com.krystianrymonlipinski.dicepouch.ui.components.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ArrowBack(onIconClicked: () -> Unit) {
    IconButton(onClick = onIconClicked) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "arrow_back",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun EditSetIcon(onIconClicked: () -> Unit) {
    IconButton(onClick = onIconClicked) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "edit_set",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun DeleteSetIcon(onIconClicked: () -> Unit) {
    IconButton(onClick = onIconClicked) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "delete_set",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
