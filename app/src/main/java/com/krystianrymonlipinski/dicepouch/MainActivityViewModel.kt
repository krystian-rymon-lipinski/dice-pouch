package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val _diceSetState: MutableStateFlow<DiceSet> = MutableStateFlow(DiceSet(BASIC_SET_NAME, basicDndDice))
    val diceSetState: StateFlow<DiceSet> = _diceSetState


    fun addNewDieToSet(numberOfSides: Int) {
        _diceSetState.update {
            it.addNewDie(Die(numberOfSides))
        }
    }

    fun deleteDieFromSet(index: Int) {
        _diceSetState.update {
            it.deleteDie(index)
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

