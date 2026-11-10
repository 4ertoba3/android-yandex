package com.github.c4ertoba3.todos.data

import com.github.c4ertoba3.todos.data.remote.TodoRemoteDataSource
import com.github.c4ertoba3.todos.todo.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoRepository(
    private val cache: RoomTodoCache,
    private val remote: TodoRemoteDataSource
) {
    val observeTodos: Flow<List<TodoItem>> = cache.observeAll()

    suspend fun refreshFromBackend() {
        val remoteTodos = remote.fetchTodos()
        cache.clear()
        cache.saveAll(remoteTodos)
    }

    suspend fun getTodo(uid: String): TodoItem? = cache.getById(uid)

    suspend fun save(item: TodoItem) {
        val isNew = cache.getById(item.uid) == null
        cache.save(item)

        if (isNew) remote.pushTodo(item)
        else remote.updateTodo(item)
    }

    suspend fun delete(uid: String) {
        cache.delete(uid)
        remote.deleteTodo(uid)
    }
}