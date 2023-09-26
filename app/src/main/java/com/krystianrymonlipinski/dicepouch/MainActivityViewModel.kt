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
            initialValue = DiceSet(BASIC_SET_NAME, basicDndDice)
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

        private val basicDndDice = listOf(
            Die(4, timestampId = 1L),
            Die(6, timestampId = 2L),
            Die(8, timestampId = 3L),
            Die(10, timestampId = 4L),
            Die(10, timestampId = 5L),
            Die(12, timestampId = 6L),
            Die(20, timestampId = 7L)
        )
    }


}

