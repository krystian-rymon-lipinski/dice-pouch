import androidx.compose.animation.animateContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun RollDescription(
    description: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = description,
        modifier = modifier.animateContentSize(),
        style = textStyle
    )
}