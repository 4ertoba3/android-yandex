package com.github.c4ertoba3.todos.data

import com.github.c4ertoba3.todos.todo.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class TodoRepository(
    private val cache: TodoCache,
    private val remote: TodoRemoteDataSource
) {
    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val observeTodos: Flow<List<TodoItem>> = _todos.asStateFlow()

    suspend fun refreshFromBackend() {
        try {
            remote.fetchTodos()
            _todos.value = cache.getAll()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getTodo(uid: String): TodoItem? = cache.getById(uid)

    suspend fun save(item: TodoItem) {
        cache.save(item)
        _todos.value = cache.getAll()
        remote.pushTodo(item)
    }

    suspend fun delete(uid: String) {
        cache.delete(uid)
        _todos.value = cache.getAll()
        remote.deleteTodo(uid)
    }
}