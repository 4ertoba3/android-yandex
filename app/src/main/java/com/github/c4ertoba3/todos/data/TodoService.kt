package com.github.c4ertoba3.todos.data

import com.github.c4ertoba3.todos.todo.FileStorage
import com.github.c4ertoba3.todos.todo.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay

class TodoService(private val storage: FileStorage) {

    suspend fun loadTodos(): List<TodoItem> = withContext(Dispatchers.IO) {
        storage.load()
        storage.todos
    }

    suspend fun deleteTodo(uid: String): Boolean = withContext(Dispatchers.IO) {
        val success = storage.remove(uid)
        if (success) storage.save()
        success
    }

    suspend fun addOrUpdateTodo(item: TodoItem) = withContext(Dispatchers.IO) {
        storage.add(item)
        storage.save()
    }
}