package com.krystianrymonlipinski.dicepouch.model

data class DiceSet(
    val name: String = "Random name",
    val dice: List<Die> = emptyList()
) {

    fun addNewDie(die: Die) : DiceSet {
        return copy(dice = dice.plus(die))
    }

    fun deleteDie(index: Int) : DiceSet {
        val updatedDice = dice.toMutableList().apply {
            this.removeAt(index)
        }
        return copy(dice = updatedDice)
    }

    fun changeName(newName: String) : DiceSet {
        return copy(name = newName)
    }

}