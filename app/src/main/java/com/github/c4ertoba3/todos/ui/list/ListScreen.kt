package com.github.c4ertoba3.todos.ui.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.c4ertoba3.todos.todo.TodoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    todos: List<TodoItem>,
    onItemClick: (TodoItem) -> Unit,
    onAddClick: () -> Unit,
    onDelete: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Мои дела") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(
                items = todos,
                key = { it.uid }
            ) { item ->
                TodoListItem(
                    item = item,
                    onClick = { onItemClick(item) },
                    onDelete = { onDelete(item) }
                )
            }
        }
    }
}