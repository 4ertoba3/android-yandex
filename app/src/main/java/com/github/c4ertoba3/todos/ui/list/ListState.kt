package com.github.c4ertoba3.todos.ui.list

import com.github.c4ertoba3.todos.todo.TodoItem

data class ListScreenState(
    val todos: List<TodoItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val latestRequestId: Int = 0
)

sealed interface ListAction {
    data class LoadStarted(val requestId: Int) : ListAction
    data class LoadSuccess(val requestId: Int, val todos: List<TodoItem>) : ListAction
    data class LoadError(val requestId: Int, val message: String) : ListAction

    data class DeleteItem(val item: TodoItem) : ListAction
    data class ItemDeleted(val uid: String) : ListAction
}