package com.github.c4ertoba3.todos.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.toArgb

@Composable
fun ColorPickerScreen(
    initialColor: Int,
    onColorSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentColor by rememberSaveable { mutableIntStateOf(initialColor) }
    var brightness by rememberSaveable { mutableFloatStateOf(1f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(currentColor))
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(16.dp))

        Text("Brightness:")
        Slider(
            value = brightness,
            onValueChange = { brightness = it },
            valueRange = 0f..1f
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Red,
                            Color.Yellow,
                            Color.Green,
                            Color.Cyan,
                            Color.Blue,
                            Color.Magenta
                        )
                    )
                )
                .pointerInput(brightness) {
                    detectDragGestures { change, _ ->
                        val x = change.position.x / size.width
                        val hue = (x * 360f).coerceIn(0f, 360f)
                        currentColor = Color.hsv(hue, 1f, brightness).toArgb()
                    }
                }
        )

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onDismiss) { Text("Отмена") }
            Spacer(Modifier.size(8.dp))
            Button(onClick = { onColorSelected(currentColor) }) { Text("Done") }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
private fun ColorPickerScreenPreview() {
    ColorPickerScreen(
        initialColor = android.graphics.Color.RED,
        onColorSelected = {},
        onDismiss = {}
    )
}