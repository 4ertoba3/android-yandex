package com.github.c4ertoba3.todos.di

import android.content.Context
import com.github.c4ertoba3.todos.data.*
import com.github.c4ertoba3.todos.todo.FileStorage

class AppContainer(context: Context) {
    private val storage = FileStorage(context)
    private val cache = TodoCache(storage)
    private val remote = TodoRemoteDataSource()

    val repository = TodoRepository(cache, remote)
}