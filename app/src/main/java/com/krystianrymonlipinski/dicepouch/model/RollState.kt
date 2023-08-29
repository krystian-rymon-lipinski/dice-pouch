package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RollState(
    val setting: RollSetting,
    val isFinished: Boolean = false,
    val currentTry: Int = 1, /* A try can have multiple throws. (Dis)advantage rolls have 2 tries. */
    val currentThrow: Int = 1,
    val tries: List<TryState> = listOf( /* TODO: don't initialize 2 tries if mechanic is normal */
        TryState(MutableList(setting.diceNumber) { null }, setting.modifier),
        TryState(MutableList(setting.diceNumber) { null }, setting.modifier)
    )
) : Parcelable {

    fun addThrow(value: Int) {
        tries[currentTry - 1].addThrow(currentThrow - 1, value)
    }

    fun calculateTryResult() {
        tries[currentTry - 1].calculateResult()
    }

}


