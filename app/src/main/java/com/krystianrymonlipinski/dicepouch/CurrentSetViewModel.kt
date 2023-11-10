package com.krystianrymonlipinski.dicepouch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSource
import com.krystianrymonlipinski.dicepouch.model.ChosenSetScreenState
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentSetViewModel @Inject constructor(
    private val setsLocalDataSource: SetsLocalDataSource,
    private val diceLocalDataSource: DiceLocalDataSource,
    private val shortcutsLocalDataSource: ShortcutsLocalDataSource,
    private val settingsLocalDataSource: SettingsLocalDataSource
) : ViewModel() {


    private val currentSetId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val chosenSetScreenState: MutableStateFlow<ChosenSetScreenState> = MutableStateFlow(ChosenSetScreenState())

    fun setCurrentSet() {
        viewModelScope.launch {
            currentSetId.value = settingsLocalDataSource.retrieveCurrentSetId()
                .take(1).singleOrNull() ?: 1 // 1 is the ID for the default set when installing the app
            .also { setId ->
                val savedSet = setsLocalDataSource.retrieveSetWithId(setId)
                    .take(1).singleOrNull()
                savedSet?.let { set ->
                    chosenSetScreenState.value = chosenSetScreenState.value.setChosenSet(set)
                }
            }

            chosenSetScreenState.value = chosenSetScreenState.value.setLoadingCompleted()
        }
    }

    fun addNewDieToSet(numberOfSides: Int) {
        viewModelScope.launch { chosenSetScreenState.value.chosenSet?.let { set ->
            diceLocalDataSource.addNewDieToSet(set.info.id, Die(numberOfSides))
        } }
    }

    fun deleteDieFromSet(die: Die) {
        viewModelScope.launch { chosenSetScreenState.value.chosenSet?.let { set ->
            diceLocalDataSource.deleteDieFromSet(set.info.id, die)
        } }
    }

    fun addNewShortcutToSet(name: String, setting: RollSetting) {
        viewModelScope.launch {
            shortcutsLocalDataSource.addNewShortcutToSet(
                RollShortcut(name = name, setting = setting)
            )
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