package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RollState(
    val setting: RollSetting,
    val currentTry: Int = 1,
    val currentThrow: Int = 1,
    val tries: List<TryState> = List(setting.numberOfTries) {
        TryState(List(setting.diceNumber) { null })
    }
) : Parcelable {

    fun updateTryWithNewThrow(throwValue: Int) : RollState {
        val updatedTries = tries.toMutableList().apply {
            this[currentTry - 1] = this[currentTry - 1].updateWithNewThrow(currentThrow - 1, throwValue)
        }
        return copy(tries = updatedTries)
    }

    fun markNextThrow() : RollState {
        return copy(currentThrow = currentThrow.inc())
    }

    fun updateTryWithResult() : RollState {
        val updatedTries = tries.toMutableList().apply {
            this[currentTry - 1] = this[currentTry - 1].updateResult(setting.modifier)
        }
        return copy(tries = updatedTries)
    }

    fun markNextTry() : RollState {
        return copy(
            currentTry = currentTry.inc(),
            currentThrow = 1
        )
    }

    fun updateTriesWithChosenOne() : RollState {
        val chosenIndex = tries.indexOf(when (setting.mechanic) {
            RollSetting.Mechanic.NORMAL -> tries.first()
            RollSetting.Mechanic.ADVANTAGE -> tries.maxBy { it.result ?: 0 }
            RollSetting.Mechanic.DISADVANTAGE -> tries.minBy { it.result ?: 0 }
        })
        val updatedTries = tries.toMutableList().apply {
            this[chosenIndex] = this[chosenIndex].updateAsChosen()
        }
        return copy(tries = updatedTries)
    }

}


