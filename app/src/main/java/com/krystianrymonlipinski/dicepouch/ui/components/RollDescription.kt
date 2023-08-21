import androidx.compose.animation.animateContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.krystianrymonlipinski.dicepouch.model.RollSetting

@Composable
fun RollDescription(
    modifier: Modifier,
    state: RollSetting
) {
    Text(
        text = state.rollDescription,
        modifier = modifier.animateContentSize(),
        style = MaterialTheme.typography.headlineLarge
    )
}