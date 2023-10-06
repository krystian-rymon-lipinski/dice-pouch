package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RollShortcut(
    val timestampId: Long = System.currentTimeMillis(),
    val name: String,
    val setting: RollSetting = RollSetting(Die(6))
) : Parcelable {

    fun changeName(newName: String) : RollShortcut {
        return copy(name = newName)
    }

    fun changeSetting(newSetting: RollSetting) : RollShortcut {
        return copy(setting = newSetting)
    }
}