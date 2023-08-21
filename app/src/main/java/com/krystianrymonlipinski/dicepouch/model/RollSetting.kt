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
    val rollDescription = buildRollDescription()

    private fun buildRollDescription() : String {
        return StringBuilder().apply {
            append(diceNumber)
            append('d')
            append(die.sides)
            when {
                modifier < 0 -> append(" - ${abs(modifier)}")
                modifier > 0 -> append(" + $modifier")
            }
        }.toString()
    }


    @Parcelize
    enum class Mechanic : Parcelable {
        ADVANTAGE, NORMAL, DISADVANTAGE;
    }
}


