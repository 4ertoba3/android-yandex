package com.github.c4ertoba3.todos.todo

import android.graphics.Color
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

val TodoItem.json: JSONObject
    get() {
        val result = JSONObject()

        result.put("text", text)
        result.put("uid", uid)
        result.put("isDone", isDone)

        if (importance != Importance.BASIC) {
            result.put("importance", importance.name)
        }

        if (color != Color.WHITE) {
            result.put("color", color)
        }

        deadline?.let {
            result.put(
                "deadline",
                it.toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        }

        return result
    }

fun TodoItem.Companion.parse(json: JSONObject): TodoItem? {
    return try {
        val text = json.optString("text", "")
        if (text.isEmpty()) return null

        val uid = json.optString("uid", "")
            .takeIf { it.isNotEmpty() }
            ?: UUID.randomUUID().toString()

        val importance = when (json.optString("importance", "BASIC")) {
            "LOW" -> Importance.LOW
            "HIGH" -> Importance.HIGH
            else -> Importance.BASIC
        }

        val color = json.optInt("color", Color.WHITE)

        val deadline = if (json.has("deadline")) {
            val millis = json.optLong("deadline", Long.MIN_VALUE)
            if (millis == Long.MIN_VALUE) null
            else LocalDateTime.ofInstant(
                Instant.ofEpochMilli(millis),
                ZoneOffset.UTC
            )
        } else null

        val isDone = json.optBoolean("isDone", false)

        TodoItem(
            text = text,
            importance = importance,
            uid = uid,
            color = color,
            deadline = deadline,
            isDone = isDone
        )
    } catch (e: Exception) {
        null
    }
}