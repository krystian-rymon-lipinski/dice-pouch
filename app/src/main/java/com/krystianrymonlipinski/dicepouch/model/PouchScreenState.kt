package com.krystianrymonlipinski.dicepouch.model

data class PouchScreenState(
    val allSets: List<DiceSetInfo>,
    val currentlyChosenSetId: Int?
)