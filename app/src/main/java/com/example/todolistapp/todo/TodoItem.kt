package com.example.todolistapp.todo

import android.graphics.Color
import java.time.LocalDateTime
import java.util.UUID

enum class Importance {
    LOW,
    BASIC,
    HIGH
}

data class TodoItem(
    val text: String,
    val importance: Importance = Importance.BASIC,
    val uid: String = UUID.randomUUID().toString(),
    val color: Int = Color.WHITE,
    val deadline: LocalDateTime? = null,
    val isDone: Boolean = false
) {
    companion object
}