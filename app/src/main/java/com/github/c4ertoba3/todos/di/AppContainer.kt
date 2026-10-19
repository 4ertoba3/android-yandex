package com.github.c4ertoba3.todos.di

import android.content.Context
import com.github.c4ertoba3.todos.data.TodoService
import com.github.c4ertoba3.todos.todo.FileStorage

class AppContainer(context: Context) {
    private val storage = FileStorage(context)
    val todoService = TodoService(storage)
}