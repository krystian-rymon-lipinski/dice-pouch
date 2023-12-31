package com.krystianrymonlipinski.dicepouch.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krystianrymonlipinski.dicepouch.R
import com.krystianrymonlipinski.dicepouch.model.Die
import com.krystianrymonlipinski.dicepouch.model.RollSetting
import com.krystianrymonlipinski.dicepouch.ui.conditionalBackground
import com.krystianrymonlipinski.dicepouch.ui.theme.DicePouchTheme
import com.krystianrymonlipinski.dicepouch.ui.theme.dieDisadvantage
import com.krystianrymonlipinski.dicepouch.ui.theme.dieAdvantage

@Composable
fun MechanicSegmentedButton(
    selectedState: RollSetting.Mechanic = RollSetting.Mechanic.NORMAL,
    onSelectedButtonChanged: (RollSetting.Mechanic) -> Unit = {},
    die: Die = Die(4)
) {
    var selectedButtonIndex by rememberSaveable { mutableIntStateOf(when (selectedState) {
        RollSetting.Mechanic.DISADVANTAGE -> 0
        RollSetting.Mechanic.NORMAL -> 1
        RollSetting.Mechanic.ADVANTAGE -> 2
    }) }

    Surface(
        modifier = Modifier
            .height(40.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(100)
            ),
        shape = RoundedCornerShape(100)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SingleOption(
                modifier = Modifier.weight(1f),
                isSelected = selectedButtonIndex == 0,
                onOptionSelected = {
                    selectedButtonIndex = 0
                    onSelectedButtonChanged(RollSetting.Mechanic.DISADVANTAGE)
                },
                die = die,
                mechanic = RollSetting.Mechanic.DISADVANTAGE
            )
            VerticalDivider()
            SingleOption(
                modifier = Modifier.weight(1f),
                isSelected = selectedButtonIndex == 1,
                onOptionSelected = {
                    selectedButtonIndex = 1
                    onSelectedButtonChanged(RollSetting.Mechanic.NORMAL)
                },
                die = die,
                mechanic = RollSetting.Mechanic.NORMAL
            )
            VerticalDivider()
            SingleOption(
                modifier = Modifier.weight(1f),
                isSelected = selectedButtonIndex == 2,
                onOptionSelected = {
                    selectedButtonIndex = 2
                    onSelectedButtonChanged(RollSetting.Mechanic.ADVANTAGE)
                },
                die = die,
                mechanic = RollSetting.Mechanic.ADVANTAGE
            )
        }
    }
}

@Composable
fun SingleOption(
    modifier: Modifier,
    isSelected: Boolean,
    onOptionSelected: () -> Unit,
    die: Die,
    mechanic: RollSetting.Mechanic
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .conditionalBackground(condition = isSelected, color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onOptionSelected() }
            .padding(horizontal = 24.dp)
            .semantics { contentDescription = "mechanics_option" },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DieImage(die = die, mechanic = mechanic)
        if (mechanic != RollSetting.Mechanic.NORMAL) {
            DieImage(die = die, mechanic = mechanic)
        }
    }
}

@Composable
fun DieImage(die: Die, mechanic: RollSetting.Mechanic) {
    Image(
        modifier = Modifier.size(18.dp),
        painter = painterResource(id = getDieImage(die.sides)),
        contentDescription = "dice",
        colorFilter = ColorFilter.tint(getDieTintColor(mechanic), blendMode = BlendMode.Modulate)
    )
}




@DrawableRes
private fun getDieImage(numberOfDice: Int) : Int {
    return when (numberOfDice) {
        4 -> R.drawable.d4
        6 -> R.drawable.d6
        8 -> R.drawable.d8
        10 -> R.drawable.d10
        12 -> R.drawable.d12
        20 -> R.drawable.d20
        else -> R.drawable.d6
    }
}

@Composable
private fun getDieTintColor(mechanic: RollSetting.Mechanic) : Color {
    return when (mechanic) {
        RollSetting.Mechanic.ADVANTAGE -> MaterialTheme.colorScheme.dieAdvantage
        RollSetting.Mechanic.NORMAL -> Color.White
        RollSetting.Mechanic.DISADVANTAGE -> MaterialTheme.colorScheme.dieDisadvantage
    }
}

@Composable
fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp),
        color = MaterialTheme.colorScheme.outline
    )
}


@Preview
@Composable
fun MechanicSegmentedButtonPreview() {
    DicePouchTheme {
        MechanicSegmentedButton()
    }
}