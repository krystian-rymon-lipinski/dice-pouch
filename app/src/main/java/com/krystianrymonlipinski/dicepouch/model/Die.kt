package com.krystianrymonlipinski.dicepouch.model

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class Die (
    val sides: Int,
    val sideColor: Color = Color.White,
    val numberColor: Color = Color.Black
) : Parcelable {

    fun roll() = Random.nextInt(from = 1, until = sides + 1)


    private companion object : Parceler<Die> {
        override fun create(parcel: Parcel): Die {
            return Die(
                parcel.readInt(),
                Color(parcel.readInt()),
                Color(parcel.readInt())
            )
        }

        override fun Die.write(parcel: Parcel, flags: Int) {
            parcel.apply {
                writeInt(sides)
                writeInt(sideColor.toArgb())
                writeInt(numberColor.toArgb())
            }
        }

    }
}