package com.github.c4ertoba3.todos.di

import android.content.Context
import androidx.room.Room
import com.github.c4ertoba3.todos.data.RoomTodoCache
import com.github.c4ertoba3.todos.data.TodoRepository
import com.github.c4ertoba3.todos.data.local.AppDatabase
import com.github.c4ertoba3.todos.data.local.MIGRATION_1_2
import com.github.c4ertoba3.todos.data.remote.TodoRemoteDataSource

class AppContainer(context: Context) {

    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "todo_database"
    )
        .addMigrations(MIGRATION_1_2)
        .build()

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "todo_database"
    ).addMigrations(MIGRATION_1_2).build()

    private val todoDao = database.todoDao()

    private val cache = RoomTodoCache(todoDao)

    private val remote = TodoRemoteDataSource(authToken = "A8B8C555-08C0-4541-80D3-3356289C0335")

    val repository = TodoRepository(cache, remote)
}