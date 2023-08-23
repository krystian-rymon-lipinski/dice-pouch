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

    fun generateModifierText() : String? {
        return when {
            modifier < 0 -> " - ${abs(modifier)}"
            modifier > 0 -> " + $modifier"
            else -> null
        }
    }

    private fun buildRollDescription() : String {
        return StringBuilder().apply {
            append(diceNumber)
            append('d')
            append(die.sides)
            generateModifierText()?.let { append(it) }

        }.toString()
    }


    @Parcelize
    enum class Mechanic : Parcelable {
        ADVANTAGE, NORMAL, DISADVANTAGE;
    }
}


