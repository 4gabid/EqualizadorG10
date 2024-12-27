package com.grupo10.equalizadorg10.commom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp




@Composable
fun FrequenciaSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,

) {
    SliderImplementation(
        label = label,
        value = value,
        onValueChange = onValueChange,
        minValueLabel = "-12 dB",
        maxValueLabel = "+12 dB",
    )

}

@Composable
fun VolumeSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,

) {
    SliderImplementation(
        label = label,
        value = value,
        onValueChange = onValueChange,
        minValueLabel = "0",
        maxValueLabel = "100",
    )

}
@Composable
fun SliderImplementation(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    minValueLabel: String = "Min",
    maxValueLabel: String = "Max"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = minValueLabel, fontSize = 12.sp, color = Color.Gray)
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..1f,
                modifier = Modifier.weight(1f)
            )
            Text(text = maxValueLabel, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
