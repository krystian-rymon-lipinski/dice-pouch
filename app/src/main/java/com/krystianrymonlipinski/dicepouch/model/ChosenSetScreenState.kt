package com.krystianrymonlipinski.dicepouch.model

data class ChosenSetScreenState(
    val isLoadingCompleted: Boolean = false,
    val chosenSet: DiceSet? = null
)