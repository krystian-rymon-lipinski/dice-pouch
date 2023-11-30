package com.krystianrymonlipinski.dicepouch.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krystianrymonlipinski.dicepouch.data_layer.DiceLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.ShortcutsLocalDataSource
import com.krystianrymonlipinski.dicepouch.model.ChosenSetScreenState
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.PouchScreenState
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.model.RollShortcut
import com.krystianrymonlipinski.dicepouch.model.RollingSettings
import com.krystianrymonlipinski.dicepouch.room.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val diceLocalDataSource: DiceLocalDataSource,
    private val shortcutsLocalDataSource: ShortcutsLocalDataSource,
    private val setsLocalDataSource: SetsLocalDataSource,
    private val settingsLocalDataSource: SettingsLocalDataSource
) : ViewModel() {


    val isLoadingFinishedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val currentSetIdState: MutableStateFlow<Int?> = MutableStateFlow(null) //TODO: refactor; to stream
    val currentSetState: MutableStateFlow<DiceSet?> = MutableStateFlow(null)

    private val allSetsInfoStream: StateFlow<List<DiceSetInfo>> = setsLocalDataSource.retrieveAllSetsInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val rollingSettingStream = settingsLocalDataSource.retrieveRollingSettings().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RollingSettings()
    )


    val tableScreenState: StateFlow<ChosenSetScreenState> = combine(
        currentSetState, isLoadingFinishedState
    ) { currentSet, isLoadingFinished ->
        ChosenSetScreenState(
            isLoadingCompleted = isLoadingFinished,
            chosenSet = currentSet
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChosenSetScreenState(isLoadingCompleted = false, chosenSet = null)
    )

    val pouchScreenState: StateFlow<PouchScreenState> = combine(
        allSetsInfoStream, currentSetIdState
    ) { allSets, currentSetId ->
        PouchScreenState(allSets, currentSetId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PouchScreenState(allSets = emptyList(), currentlyChosenSetId = null)
    )

    fun initiateCurrentSet() {
        viewModelScope.launch(Dispatchers.IO) {
            currentSetIdState.value = (settingsLocalDataSource.retrieveCurrentSetId()
                .take(1).singleOrNull() ?: AppDatabase.DEFAULT_SET_ID
            ).also { setId ->
                val savedSet = setsLocalDataSource.retrieveSetWithId(setId).take(1).singleOrNull()
                currentSetState.value = savedSet
                println("set = ${currentSetState.value}")
            }
            isLoadingFinishedState.value = true
        }
    }

    fun addNewDieToSet(chosenSetId: Int, numberOfSides: Int) {
        viewModelScope.launch {
            diceLocalDataSource.addNewDieToSet(chosenSetId, Die(numberOfSides))
            refreshSetState()
        }
    }

    fun deleteDieFromSet(chosenSetId: Int, die: Die) {
        viewModelScope.launch {
            diceLocalDataSource.deleteDieFromSet(chosenSetId, die)
            refreshSetState()
        }
    }

    fun addNewShortcutToSet(name: String, setting: RollSetting) {
        viewModelScope.launch {
            shortcutsLocalDataSource.addNewShortcutToSet(
                RollShortcut(name = name, setting = setting)
            )
            refreshSetState()
        }
    }

    fun updateShortcut(shortcutWithChanges: RollShortcut) {
        viewModelScope.launch {
            shortcutsLocalDataSource.updateShortcut(shortcutWithChanges)
            refreshSetState()
        }
    }

    fun deleteShortcut(shortcut: RollShortcut) {
        viewModelScope.launch {
            shortcutsLocalDataSource.deleteShortcutFromSet(shortcut)
            refreshSetState()
        }
    }

    fun addNewSet(name: String, diceColor: Color, numbersColor: Color) {
        viewModelScope.launch {
            setsLocalDataSource.addDiceSet(DiceSetInfo(
                name = name,
                diceColor = diceColor,
                numbersColor = numbersColor))
        }
    }

    fun changeChosenSet(chosenSetId: Int) {
        viewModelScope.launch {
            settingsLocalDataSource.changeCurrentSet(chosenSetId)
            refreshSetIdState()
            refreshSetState()
        }
    }

    fun deleteSet(set: DiceSetInfo) {
        viewModelScope.launch {
            setsLocalDataSource.deleteDiceSet(set)
        }
    }

    fun saveSettings(settings: RollingSettings) {
        viewModelScope.launch {
            settingsLocalDataSource.saveRollingSettings(settings)
        }
    }

    fun retrieveSettings() : RollingSettings {
        var settings = RollingSettings()
        viewModelScope.launch {
            settings = settingsLocalDataSource.retrieveRollingSettings().take(1).single()
        }
        return settings
    }

    fun changeSetName(setInfo: DiceSetInfo?, newName: String) {
        setInfo?.let { viewModelScope.launch {
            setsLocalDataSource.changeSetName(setInfo, newName)
        } }
    }

    fun retrieveSetWithChosenId(id: Int) : Flow<DiceSet?> {
        return setsLocalDataSource.retrieveSetWithId(id)
    }

    private suspend fun refreshSetIdState() {
        currentSetIdState.value = settingsLocalDataSource.retrieveCurrentSetId().take(1).singleOrNull()
    }

    private suspend fun refreshSetState() {
        //TODO: try refactoring it using .stateIn operator
        currentSetIdState.value?.let { setId ->
            val currentSetState = setsLocalDataSource.retrieveSetWithId(setId).take(1).single()
            this@MainActivityViewModel.currentSetState.value = currentSetState
        }
    }
}