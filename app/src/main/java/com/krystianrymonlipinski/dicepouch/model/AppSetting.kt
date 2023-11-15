package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppSetting(
    val isSoundOn: Boolean = false,
    val singleThrowTimeMillis: Int = 1000,
    val delayBetweenThrowsTimeMillis: Int = 500,
    val isRollPopupAutodismissOn: Boolean = false,
    val rollPopupAutodismissTimeMillis: Int = 500
) : Parcelable {

    fun setIsSoundOn(isOn: Boolean) : AppSetting {
        return copy(isSoundOn = isOn)
    }

    fun setSingleThrowTimeMillis(timeMillis: Int) : AppSetting {
        return copy(singleThrowTimeMillis = timeMillis)
    }

    fun setDelayBetweenThrowTimeMillis(timeMillis: Int) : AppSetting {
        return copy(delayBetweenThrowsTimeMillis = timeMillis)
    }

    fun setIsRollPopupAutodismissOn(isOn: Boolean) : AppSetting {
        return copy(isRollPopupAutodismissOn = isOn)
    }

    fun setRollPopupAutodismissTimeMillis(timeMillis: Int) : AppSetting {
        return copy(rollPopupAutodismissTimeMillis = timeMillis)
    }
}