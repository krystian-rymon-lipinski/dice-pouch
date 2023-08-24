package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RollState(
    val progress: Progress = Progress.NOT_STARTED,
    val currentTry: Int = 1, /* 1 try can have multiple throws. (Dis)advantage rolls have 2 tries. */
    val currentThrow: Int = 1,
    val throwResults: List<MutableList<Int?>> = listOf(mutableListOf(), mutableListOf())
) : Parcelable {

    @Parcelize
    enum class Progress : Parcelable {
        NOT_STARTED, IN_PROGRESS, FINISHED
    }
}


