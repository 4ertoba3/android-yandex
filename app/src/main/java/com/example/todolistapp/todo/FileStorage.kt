package com.example.todolistapp.todo

import android.content.Context
import org.json.JSONArray
import java.io.File
import java.time.LocalDateTime

class FileStorage(
    private val context: Context
) {
    private val items = mutableListOf<TodoItem>()

    // read-only снаружи, без побочных эффектов
    val todos: List<TodoItem>
        get() = items.toList()

    private val file: File
        get() = File(context.filesDir, "todos.json")

    // upsert: если uid уже есть — заменяем, иначе добавляем
    fun add(item: TodoItem) {
        val index = items.indexOfFirst { it.uid == item.uid }
        if (index >= 0) items[index] = item
        else items.add(item)
    }

    // true — если что-то удалили, false — если uid не найден
    fun remove(uid: String): Boolean {
        return items.removeAll { it.uid == uid }
    }

    fun save() {
        purgeExpired()

        val jsonArray = JSONArray()
        for (item in items) {
            jsonArray.put(item.json)
        }
        file.writeText(jsonArray.toString())
    }

    fun load() {
        if (!file.exists()) return  // первый запуск — норма

        val parsed = try {
            val jsonArray = JSONArray(file.readText())
            val temp = mutableListOf<TodoItem>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.optJSONObject(i) ?: continue
                val item = TodoItem.parse(jsonObject) ?: continue
                temp.add(item)
            }
            temp
        } catch (e: Exception) {
            // битый файл — не теряем уже загруженное
            return
        }

        items.clear()
        items.addAll(parsed)
        purgeExpired()
    }

    // Усложнение: удаляем просроченные НЕвыполненные дела
    private fun purgeExpired() {
        val now = LocalDateTime.now()
        items.removeAll { item ->
            val deadline = item.deadline
            deadline != null && deadline.isBefore(now) && !item.isDone
        }
    }
}