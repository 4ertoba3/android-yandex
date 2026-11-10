package com.github.c4ertoba3.todos.di

import android.content.Context
import com.github.c4ertoba3.todos.data.TodoCache
import com.github.c4ertoba3.todos.data.TodoRepository
import com.github.c4ertoba3.todos.data.remote.TodoRemoteDataSource
import com.github.c4ertoba3.todos.todo.FileStorage

class AppContainer(context: Context) {
    private val storage = FileStorage(context)
    private val cache = TodoCache(storage)

    private val remote = TodoRemoteDataSource(authToken = "A8B8C555-08C0-4541-80D3-3356289C0335")

    val repository = TodoRepository(cache, remote)
}