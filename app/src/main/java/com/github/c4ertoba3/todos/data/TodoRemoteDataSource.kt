package com.github.c4ertoba3.todos.data

import android.util.Log
import com.github.c4ertoba3.todos.todo.TodoItem
import kotlinx.coroutines.delay
import java.io.IOException

class TodoRemoteDataSource {

    suspend fun fetchTodos(): List<TodoItem> {
        Log.d("TODO_REMOTE", "fetchTodos: запрос на сервер")
        delay(1000)
        // if ((0..2).random() == 0) throw IOException("Ошибка сети")
        return emptyList()
    }

    suspend fun pushTodo(item: TodoItem) {
        Log.d("TODO_REMOTE", "pushTodo: отправка на сервер -> $item")
        delay(500)
    }

    suspend fun deleteTodo(uid: String) {
        Log.d("TODO_REMOTE", "deleteTodo: удаление с сервера -> $uid")
        delay(500)
    }
}