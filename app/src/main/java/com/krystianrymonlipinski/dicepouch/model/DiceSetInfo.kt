package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiceSetInfo(
    val id: Int = 0,
    val name: String = "Set name",
    val diceColor: Color = Color.White,
    val numbersColor: Color = Color.Black
) : Parcelable {

    fun changeName(newName: String) = copy(name = newName)
    fun changeDiceColor(newColor: Color) = copy(diceColor = newColor)
    fun changeNumbersColor(newColor: Color) = copy(numbersColor = newColor)

    private companion object : Parceler<DiceSetInfo> {
        override fun create(parcel: Parcel): DiceSetInfo {
            return DiceSetInfo(
                parcel.readInt(),
                parcel.readString() ?: "",
                Color(parcel.readInt()),
                Color(parcel.readInt())
            )
        }

        override fun DiceSetInfo.write(parcel: Parcel, flags: Int) {
            parcel.apply {
                writeInt(id)
                writeString(name)
                writeInt(diceColor.toArgb())
                writeInt(numbersColor.toArgb())
            }
        }
    }
}