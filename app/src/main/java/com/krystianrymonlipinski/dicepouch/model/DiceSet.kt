package com.krystianrymonlipinski.dicepouch.model

data class DiceSet(
    val id: Int = 0,
    val name: String = "Random name",
    val dice: List<Die> = emptyList(),
    val shortcuts: List<RollShortcut> = emptyList()
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

    fun addNewShortcut(newShortcut: RollShortcut) : DiceSet {
        return copy(shortcuts = shortcuts.plus(newShortcut))
    }

    fun changeShortcut(index: Int, changedShortcut: RollShortcut) : DiceSet {
        val updatedShortcuts = shortcuts.toMutableList().apply {
            this[index] = changedShortcut
        }
        return copy(shortcuts = updatedShortcuts)
    }

    fun deleteShortcut(index: Int) : DiceSet {
        val updatedShortcuts = shortcuts.toMutableList().apply {
            this.removeAt(index)
        }
        return copy(shortcuts = updatedShortcuts)
    }

}