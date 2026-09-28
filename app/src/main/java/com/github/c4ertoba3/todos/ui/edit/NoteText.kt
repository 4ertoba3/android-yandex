package com.github.c4ertoba3.todos.ui.edit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.c4ertoba3.todos.ui.theme.ToDosTheme

@Composable
fun NoteText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Дело") },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        minLines = 2,
        maxLines = Int.MAX_VALUE
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteTextPreview() {
    ToDosTheme {
        NoteText(value = "Купить хлеб", onValueChange = {})
    }
}