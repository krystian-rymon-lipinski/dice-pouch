package com.krystianrymonlipinski.dicepouch

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun DicePouchTabRow(
    selectedTabIndex: Int,
    onTabClicked: (Int) -> Unit,
) {
    TabRow(selectedTabIndex = selectedTabIndex) {
        Tab(
            selected = selectedTabIndex == 0,
            onClick = { onTabClicked(0) },
            text = { Text(text = stringResource(R.string.tab_table)) }
        )
        Tab(
            selected = selectedTabIndex == 1,
            onClick = { onTabClicked(1) },
            text = { Text(text = stringResource(id = R.string.tab_pouch)) }
        )
    }
}
