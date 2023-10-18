package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSource
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.room.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val setsLocalDataSource: SetsLocalDataSource,
    private val diceLocalDataSource: DiceLocalDataSource,
    private val shortcutsLocalDataSource: ShortcutsLocalDataSource
) : ViewModel() {


    val diceSetState: StateFlow<DiceSet> = setsLocalDataSource.retrieveSetWithName(AppDatabase.DEFAULT_SET_NAME)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DiceSet()
        )

    val allSetsState: StateFlow<List<DiceSetInfo>> = setsLocalDataSource.retrieveAllSetsInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    fun addNewSet(name: String, diceColor: Color, numbersColor: Color) {
        viewModelScope.launch {
            setsLocalDataSource.addDiceSet(DiceSetInfo(
                name = name,
                diceColor = diceColor,
                numbersColor = numbersColor))
        }
    }

    fun deleteSet(set: DiceSetInfo) { //TODO: prevent deleting if there's only one set
        viewModelScope.launch {
            setsLocalDataSource.deleteDiceSet(set)
        }
    }

    fun addNewDieToSet(numberOfSides: Int) {
        viewModelScope.launch {
            diceLocalDataSource.addNewDieToSet(diceSetState.value.info.id, Die(numberOfSides))
        }
    }

    fun deleteDieFromSet(die: Die) {
        viewModelScope.launch {
            diceLocalDataSource.deleteDieFromSet(diceSetState.value.info.id, die)
        }
    }

    fun addNewShortcutToSet(name: String, setting: RollSetting) {
        viewModelScope.launch {
            shortcutsLocalDataSource.addNewShortcutToSet(RollShortcut(
                name = name,
                setting = setting
            ))
        }
    }

    fun updateShortcut(shortcutWithChanges: RollShortcut) {
        viewModelScope.launch {
            shortcutsLocalDataSource.updateShortcut(shortcutWithChanges)
        }
    }

    fun deleteShortcut(shortcut: RollShortcut) {
        viewModelScope.launch {
            shortcutsLocalDataSource.deleteShortcutFromSet(shortcut)
        }
    }


}

