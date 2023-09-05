package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TryState(
    val throws: List<Int?>,
    val result: Int? = null,
    val isChosen: Boolean = false
) : Parcelable {

    fun updateWithNewThrow(index: Int, value: Int) : TryState {
        val updatedThrows = throws.toMutableList()
        updatedThrows[index] = value
        return copy(throws = updatedThrows)
    }

    fun updateResult(modifier: Int): TryState {
        return copy(result = calculateThrowsSum()?.plus(modifier))
    }


    fun updateAsChosen() : TryState {
        return copy(isChosen = true)
    }

    private fun calculateThrowsSum() : Int? {
        return if (isTryFinished()) throws.filterNotNull().sumOf { it }
        else null
    }
    private fun isTryFinished() = !throws.contains(null)
}