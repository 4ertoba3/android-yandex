package com.github.c4ertoba3.todos.ui.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.c4ertoba3.todos.todo.Importance
import com.github.c4ertoba3.todos.todo.TodoItem
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun EditScreen(
    viewModel: EditViewModel,
    onSaveComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initialItem by viewModel.initialItem.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var text by rememberSaveable { mutableStateOf("") }
    var isDone by rememberSaveable { mutableStateOf(false) }
    var color by rememberSaveable { mutableStateOf(android.graphics.Color.WHITE) }
    var importance by rememberSaveable { mutableStateOf(Importance.BASIC) }
    var deadline by rememberSaveable { mutableStateOf<LocalDateTime?>(null) }

    var showColorPicker by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(initialItem) {
        initialItem?.let { item ->
            text = item.text
            isDone = item.isDone
            color = item.color
            importance = item.importance
            deadline = item.deadline
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { millis ->
                deadline = millis?.let {
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
                }
            },
            onDismiss = { showDatePicker = false }
        )
    }

    val containerColor = if (isDone) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = containerColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .imePadding()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    NoteText(
                        value = text,
                        onValueChange = { text = it }
                    )
                    Spacer(Modifier.height(16.dp))
                    DatePickerRow(
                        deadline = deadline,
                        onClick = { showDatePicker = true }
                    )
                    Spacer(Modifier.height(16.dp))
                    DoneCheckbox(
                        checked = isDone,
                        onCheckedChange = { isDone = it }
                    )
                    Spacer(Modifier.height(16.dp))
                    ColorPickerRow(
                        selectedColor = color,
                        onColorSelected = { color = it },
                        onCustomColorClick = { showColorPicker = true }
                    )
                    Spacer(Modifier.height(16.dp))
                    ImportanceRow(
                        selected = importance,
                        onSelect = { importance = it }
                    )
                    Spacer(Modifier.height(16.dp))
                }

                Button(
                    onClick = {
                        val item = (initialItem ?: TodoItem(text = text)).copy(
                            text = text,
                            importance = importance,
                            color = color,
                            deadline = deadline,
                            isDone = isDone
                        )
                        viewModel.save(item)
                        onSaveComplete()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить")
                }
            }
        }
    }

    if (showColorPicker) {
        ColorPickerScreen(
            initialColor = color,
            onColorSelected = {
                color = it
                showColorPicker = false
            },
            onDismiss = { showColorPicker = false }
        )
    }
}