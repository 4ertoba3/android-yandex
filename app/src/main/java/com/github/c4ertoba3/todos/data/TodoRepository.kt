package com.github.c4ertoba3.todos.data

import com.github.c4ertoba3.todos.data.remote.TodoRemoteDataSource
import com.github.c4ertoba3.todos.todo.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoRepository(
    private val cache: TodoCache,
    private val remote: TodoRemoteDataSource
) {
    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val observeTodos: Flow<List<TodoItem>> = _todos.asStateFlow()

    suspend fun refreshFromBackend() {
        try {
            val remoteTodos = remote.fetchTodos()

            remoteTodos.forEach { cache.save(it) }

            _todos.value = cache.getAll()
        } catch (e: Exception) {
            _todos.value = cache.getAll()
            throw e
        }
    }

    suspend fun getTodo(uid: String): TodoItem? = cache.getById(uid)

    suspend fun save(item: TodoItem) {
        val isNew = cache.getById(item.uid) == null
        cache.save(item)
        _todos.value = cache.getAll()

        if (isNew) remote.pushTodo(item)
        else remote.updateTodo(item)
    }

    suspend fun delete(uid: String) {
        cache.delete(uid)
        _todos.value = cache.getAll()
        remote.deleteTodo(uid)
    }
}