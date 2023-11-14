package com.krystianrymonlipinski.dicepouch.ui

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.krystianrymonlipinski.dicepouch.R

@Composable
fun DicePouchTabRow(
    selectedTabIndex: Int,
    onTabClicked: (Int) -> Unit,
) {
    TabRow(selectedTabIndex = selectedTabIndex) {
        Tab(
            selected = selectedTabIndex == TAB_TABLE,
            onClick = { if (selectedTabIndex != TAB_TABLE) onTabClicked(TAB_TABLE) },
            text = { Text(text = stringResource(R.string.tab_table)) }
        )
        Tab(
            selected = selectedTabIndex == TAB_POUCH,
            onClick = { if (selectedTabIndex != TAB_POUCH) onTabClicked(TAB_POUCH) },
            text = { Text(text = stringResource(id = R.string.tab_pouch)) }
        )
    }
}

const val TAB_TABLE = 0
const val TAB_POUCH = 1
const val TAB_SETTINGS = 2
