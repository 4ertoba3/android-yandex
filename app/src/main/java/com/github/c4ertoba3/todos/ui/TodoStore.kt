package com.github.c4ertoba3.todos.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.c4ertoba3.todos.todo.FileStorage
import com.github.c4ertoba3.todos.todo.TodoItem

class TodoStore(private val storage: FileStorage) {

    var todos by mutableStateOf(storage.todos)
        private set

    fun add(item: TodoItem) {
        storage.add(item)
        storage.save()
        todos = storage.todos
    }

    fun remove(uid: String) {
        storage.remove(uid)
        storage.save()
        todos = storage.todos
    }

    fun findById(uid: String?): TodoItem? {
        if (uid == null) return null
        return todos.find { it.uid == uid }
    }
}