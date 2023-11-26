package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SecondaryCaptionWithIcon(
    text: String,
    imageVector: ImageVector,
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
            IconButton(onClick = onIconClicked) {
                Icon( //TODO: improve all icon buttons; for their selected state is not highlighted; also use icons, not images
                    imageVector = imageVector,
                    contentDescription = "secondary_caption_icon",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
        //Spacer(modifier = Modifier.height(2.dp))
        Divider(
            modifier = Modifier.height(2.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.secondary
        )
    }

}