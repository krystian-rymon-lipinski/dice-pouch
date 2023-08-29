package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TryState(
    val throws: MutableList<Int?>,
    val modifier: Int,
    var result: Int? = null
) : Parcelable {

    fun addThrow(index: Int, value: Int) {
        throws[index] = value
    }

    fun calculateResult() {
        result =
            if (isTryFinished()) throws.filterNotNull().sumOf { it } + modifier
            else null
    }

    private fun isTryFinished() = !throws.contains(null)
}