package com.krystianrymonlipinski.dicepouch.model

import androidx.compose.ui.graphics.Color

data class DiceSetInfo(
    val id: Int = 0,
    val name: String = "Set name",
    val diceColor: Color = Color.White,
    val numbersColor: Color = Color.Black
) {

    fun changeName(newName: String) = copy(name = newName)
    fun changeDiceColor(newColor: Color) = copy(diceColor = newColor)
    fun changeNumbersColor(newColor: Color) = copy(numbersColor = newColor)
}