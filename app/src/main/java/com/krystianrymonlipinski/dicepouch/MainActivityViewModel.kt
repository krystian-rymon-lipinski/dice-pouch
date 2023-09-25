package com.krystianrymonlipinski.dicepouch

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.krystianrymonlipinski.dicepouch.model.DiceSet
import com.krystianrymonlipinski.dicepouch.model.Die
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val _diceSetState: MutableStateFlow<DiceSet> = MutableStateFlow(DiceSet(BASIC_SET_NAME, basicDndDice))
    val diceSetState: StateFlow<DiceSet> = _diceSetState


    fun addNewDieToSet(numberOfSides: Int) {
        //TODO: implement adding die
    }

    fun deleteDieFromSet() {
        //TODO: implement deleting die
    }



    companion object {
        private const val BASIC_SET_NAME = "Basic D&D Set"

        private val basicDndDice = listOf(
            Die(4),
            Die(6, Color.Green, Color.Red),
            Die(8),
            Die(10),
            Die(10),
            Die(12),
            Die(20)
        )
    }


}

