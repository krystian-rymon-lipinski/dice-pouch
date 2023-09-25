package com.krystianrymonlipinski.dicepouch.model

data class DiceSet(
    val name: String = "Random name",
    val dice: List<Die> = emptyList()
)