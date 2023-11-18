package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RollingSettings(
    val isSoundOn: Boolean = false,
    val singleThrowTimeMillis: Int = 1000,
    val delayBetweenThrowsTimeMillis: Int = 500,
    val isRollPopupAutodismissOn: Boolean = false,
    val rollPopupAutodismissTimeMillis: Int = 500
) : Parcelable {

    fun setIsSoundOn(isOn: Boolean) : RollingSettings {
        return copy(isSoundOn = isOn)
    }

    fun setSingleThrowTimeMillis(timeMillis: Int) : RollingSettings {
        return copy(singleThrowTimeMillis = timeMillis)
    }

    fun setDelayBetweenThrowTimeMillis(timeMillis: Int) : RollingSettings {
        return copy(delayBetweenThrowsTimeMillis = timeMillis)
    }

    fun setIsRollPopupAutodismissOn(isOn: Boolean) : RollingSettings {
        return copy(isRollPopupAutodismissOn = isOn)
    }

    fun setRollPopupAutodismissTimeMillis(timeMillis: Int) : RollingSettings {
        return copy(rollPopupAutodismissTimeMillis = timeMillis)
    }
}