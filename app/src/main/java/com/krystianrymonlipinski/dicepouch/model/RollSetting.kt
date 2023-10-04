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

    fun changeDiceNumber(newValue: Int) : RollSetting {
        return copy(diceNumber = newValue)
    }

    fun changeModifier(newValue: Int) : RollSetting {
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
}


