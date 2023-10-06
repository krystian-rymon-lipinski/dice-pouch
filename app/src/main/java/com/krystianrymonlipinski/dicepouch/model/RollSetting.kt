package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.math.abs

@Parcelize
data class RollSetting(
    val die: Die,
    val diceNumber: Int = 1,
    val modifier: Int = 0,
    val mechanic: Mechanic = Mechanic.NORMAL
) : Parcelable {

    @IgnoredOnParcel
    val numberOfTries = if (mechanic == Mechanic.NORMAL) 1 else 2
    @IgnoredOnParcel
    val rollDescription = buildRollDescription()

    fun changeDie(newDie: Die) : RollSetting {
        return copy(die = newDie)
    }

    fun changeDiceNumber(change: Int) : RollSetting {
        var newValue = diceNumber + change
        newValue =
            if (newValue > Constraint.MAX_DICE) Constraint.MIN_DICE
            else if (newValue < Constraint.MIN_DICE) Constraint.MAX_DICE
            else newValue
        return copy(diceNumber = newValue)
    }

    fun changeModifier(change: Int) : RollSetting {
        var newValue = modifier + change
        newValue =
            if (newValue > Constraint.MAX_MOD) Constraint.MIN_MOD
            else if (newValue < Constraint.MIN_MOD) Constraint.MAX_MOD
            else newValue
        return copy(modifier = newValue)
    }

    fun changeMechanic(newValue: Mechanic) : RollSetting {
        return copy(mechanic = newValue)
    }


    private fun generateModifierDescription() : String? {
        return when {
            modifier < 0 -> " - ${abs(modifier)}"
            modifier > 0 -> " + $modifier"
            else -> null
        }
    }

    private fun buildRollDescription() : String {
        return StringBuilder().apply {
            append("${diceNumber}d${die.sides}")
            generateModifierDescription()?.let { append(it) }
            when (mechanic) {
                Mechanic.ADVANTAGE -> append(" (A)")
                Mechanic.DISADVANTAGE -> append(" (D)")
                else -> { }
            }

        }.toString()
    }


    @Parcelize
    enum class Mechanic : Parcelable {
        ADVANTAGE, NORMAL, DISADVANTAGE;

        companion object {
            fun fromString(string: String) : Mechanic {
                return when(string) {
                    "ADVANTAGE" -> ADVANTAGE
                    "NORMAL" -> NORMAL
                    "DISADVANTAGE" -> DISADVANTAGE
                    else -> NORMAL
                }
            }
        }
    }

    object Constraint {
        const val MIN_DICE = 1
        const val MAX_DICE = 30
        const val MIN_MOD = -30
        const val MAX_MOD = 30
    }
}


