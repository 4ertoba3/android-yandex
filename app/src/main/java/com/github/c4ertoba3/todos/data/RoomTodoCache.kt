package com.github.c4ertoba3.todos.data

import com.github.c4ertoba3.todos.data.local.TodoDao
import com.github.c4ertoba3.todos.data.local.TodoEntity
import com.github.c4ertoba3.todos.todo.Importance
import com.github.c4ertoba3.todos.todo.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomTodoCache(private val dao: TodoDao) {

    fun observeAll(): Flow<List<TodoItem>> {
        return dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getById(uid: String): TodoItem? = dao.getById(uid)?.toDomain()

    suspend fun save(item: TodoItem) {
        dao.insert(item.toEntity())
    }

    suspend fun saveAll(items: List<TodoItem>) {
        dao.insertAll(items.map { it.toEntity() })
    }

    suspend fun delete(uid: String) {
        dao.deleteById(uid)
    }

    suspend fun clear() {
        dao.clearAll()
    }

    // Мапперы
    private fun TodoEntity.toDomain() = TodoItem(
        uid = uid,
        text = text,
        importance = Importance.entries[importance],
        color = color,
        deadline = deadline?.let {
            java.time.LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(it),
                java.time.ZoneOffset.UTC
            )
        },
        isDone = isDone
    )

    private fun TodoItem.toEntity() = TodoEntity(
        uid = uid,
        text = text,
        importance = importance.ordinal,
        color = color,
        deadline = deadline?.let {
            it.toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
        },
        isDone = isDone
    )
}