package com.krystianrymonlipinski.dicepouch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSource
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val localDataSource: DiceSetLocalDataSource,
    private val shortcutsLocalDataSource: ShortcutsLocalDataSource
) : ViewModel() {

    private val _setName = MutableStateFlow(BASIC_SET_NAME)
    private val _diceStream = localDataSource.getDiceStream()
    private val _shortcutsStream = shortcutsLocalDataSource.getShortcutsStream()

    val diceSetState: StateFlow<DiceSet> = combine(
        _setName, _diceStream, _shortcutsStream
    ) { setName, diceList, shortcutsList ->
        DiceSet(setName, diceList, shortcutsList)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DiceSet(BASIC_SET_NAME, emptyList(), emptyList())
        )


    fun addNewDieToSet(numberOfSides: Int) {
        viewModelScope.launch {
            localDataSource.addNewDieToSet(Die(numberOfSides))
        }
    }

    fun deleteDieFromSet(index: Int) {
        viewModelScope.launch {
            localDataSource.deleteDieFromSet(diceSetState.value.dice[index])
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

    fun deleteShortcut(index: Int) {
        viewModelScope.launch {
            shortcutsLocalDataSource.deleteShortcutFromSet(diceSetState.value.shortcuts[index])
        }
    }


    companion object {
        private const val BASIC_SET_NAME = "Basic D&D Set"
    }

}

