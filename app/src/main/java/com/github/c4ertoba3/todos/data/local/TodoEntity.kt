package com.github.c4ertoba3.todos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey val uid: String,
    val text: String,
    val importance: Int,
    val color: Int,
    val deadline: Long?,
    val isDone: Boolean
)