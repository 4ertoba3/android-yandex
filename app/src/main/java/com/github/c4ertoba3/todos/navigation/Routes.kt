package com.github.c4ertoba3.todos.navigation

object Routes {
    const val LIST = "list"
    const val EDIT = "edit?todoId={todoId}"
    const val EDIT_BASE = "edit"
    const val ARG_TODO_ID = "todoId"

    fun edit(todoId: String? = null): String =
        if (todoId == null) EDIT_BASE
        else "$EDIT_BASE?$ARG_TODO_ID=$todoId"
}