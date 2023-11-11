package com.krystianrymonlipinski.dicepouch.model

data class ChosenSetScreenState(
    val isLoadingCompleted: Boolean = false,
    val chosenSet: DiceSet? = null
) {

    fun setLoadingCompleted() : ChosenSetScreenState {
        return copy(isLoadingCompleted = true)
    }

    fun setChosenSet(chosenSet: DiceSet?) : ChosenSetScreenState {
        return copy(chosenSet = chosenSet)
    }
}