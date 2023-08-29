package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RollState(
    val setting: RollSetting,
    val progress: Progress = Progress.NOT_STARTED,
    val currentTry: Int = 1, /* A try can have multiple throws. (Dis)advantage rolls have 2 tries. */
    val currentThrow: Int = 1,
    val tries: List<TryState> = listOf(
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


    @Parcelize
    enum class Progress : Parcelable {
        NOT_STARTED, IN_PROGRESS, FINISHED
    }
}


