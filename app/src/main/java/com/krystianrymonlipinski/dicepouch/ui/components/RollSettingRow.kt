package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krystianrymonlipinski.dicepouch.ui.components.icons.MinusIcon
import com.krystianrymonlipinski.dicepouch.ui.components.icons.PlusIcon

@Composable
fun RollSettingRow(
    settingName: String,
    onIncrementClicked: () -> Unit,
    onDecrementClicked: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MinusIcon(onIconClicked = { onDecrementClicked() })
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = settingName
        )
        PlusIcon(onIconClicked = { onIncrementClicked() })
    }
}