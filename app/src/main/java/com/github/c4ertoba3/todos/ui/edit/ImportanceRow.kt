package com.github.c4ertoba3.todos.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.c4ertoba3.todos.todo.Importance

@Composable
fun ImportanceRow(
    selected: Importance,
    onSelect: (Importance) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Важность:",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Importance.entries.forEach { imp ->
                FilterChip(
                    selected = selected == imp,
                    onClick = { onSelect(imp) },
                    label = { Text(imp.name) }
                )
            }
        }
    }
}