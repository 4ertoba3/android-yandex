package com.github.c4ertoba3.todos.data

import com.github.c4ertoba3.todos.todo.FileStorage
import com.github.c4ertoba3.todos.todo.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoCache(private val storage: FileStorage) {

    suspend fun getAll(): List<TodoItem> = withContext(Dispatchers.IO) {
        storage.load()
        storage.todos
    }

    suspend fun getById(uid: String): TodoItem? = withContext(Dispatchers.IO) {
        storage.todos.find { it.uid == uid }
    }

    suspend fun save(item: TodoItem) = withContext(Dispatchers.IO) {
        storage.add(item)
        storage.save()
    }

    suspend fun delete(uid: String) = withContext(Dispatchers.IO) {
        storage.remove(uid)
        storage.save()
    }
}