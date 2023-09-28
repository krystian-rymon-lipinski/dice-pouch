package com.krystianrymonlipinski.dicepouch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
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
    private val localDataSource: DiceSetLocalDataSource
) : ViewModel() {

    private val _setName = MutableStateFlow(BASIC_SET_NAME)
    private val _diceStream = localDataSource.getDiceStream()

    val diceSetState: StateFlow<DiceSet> = combine(
        _setName, _diceStream
    ) { setName, diceList ->
        DiceSet(setName, diceList)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DiceSet(BASIC_SET_NAME, emptyList())
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


    companion object {
        private const val BASIC_SET_NAME = "Basic D&D Set"
    }

}

