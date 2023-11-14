package com.krystianrymonlipinski.dicepouch.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krystianrymonlipinski.dicepouch.data_layer.SetsLocalDataSource
import com.krystianrymonlipinski.dicepouch.data_layer.SettingsLocalDataSource
import com.krystianrymonlipinski.dicepouch.model.DiceSetInfo
import com.krystianrymonlipinski.dicepouch.model.PouchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PouchViewModel @Inject constructor(
    private val setsLocalDataSource: SetsLocalDataSource,
    private val settingsLocalDataSource: SettingsLocalDataSource
) : ViewModel() {

    private val currentSetId: StateFlow<Int?> = settingsLocalDataSource.retrieveCurrentSetId()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val allSetsState: StateFlow<PouchScreenState> = combine(
        setsLocalDataSource.retrieveAllSetsInfo(),
        currentSetId
    ) { setsFlowState, setNameState ->
        PouchScreenState(setsFlowState, setNameState)
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PouchScreenState(emptyList(), null)
    )


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
        }
    }

    fun deleteSet(set: DiceSetInfo) { //TODO: prevent deleting if there's only one set
        viewModelScope.launch {
            setsLocalDataSource.deleteDiceSet(set)
        }
    }


}

