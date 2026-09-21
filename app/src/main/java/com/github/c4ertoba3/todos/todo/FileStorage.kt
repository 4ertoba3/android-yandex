package com.github.c4ertoba3.todos.todo

import android.content.Context
import org.json.JSONArray
import java.io.File
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import java.io.IOException

class FileStorage(
    private val context: Context
) {
    private val log = LoggerFactory.getLogger(FileStorage::class.java)
    private val items = mutableListOf<TodoItem>()

    val todos: List<TodoItem>
        get() = items.toList()

    private val file: File
        get() = File(context.filesDir, "todos.json")

    fun add(item: TodoItem) {
        val index = items.indexOfFirst { it.uid == item.uid }
        if (index >= 0) items[index] = item
        else items.add(item)
    }

    fun remove(uid: String): Boolean {
        return items.removeAll { it.uid == uid }
    }

    fun save() {
        log.info("save: start, {} items", items.size)
        try {
            purgeExpired()

            val jsonArray = JSONArray()
            for (item in items) {
                jsonArray.put(item.json)
            }
            file.writeText(jsonArray.toString())

            log.info("save: done, {} bytes", file.length())
        } catch (e: IOException) {
            log.error("save: failed", e)
        }
    }

    fun load() {
        log.info("load: start")

        if (!file.exists()) {
            log.warn("load: file does not exist, returning empty list")
            return
        }

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
            log.error("load: failed to parse file", e)
            return
        }

        items.clear()
        items.addAll(parsed)
        purgeExpired()

        log.info("load: done, {} items restored", items.size)
    }

    private fun purgeExpired() {
        val now = LocalDateTime.now()
        items.removeAll { item ->
            val deadline = item.deadline
            deadline != null && deadline.isBefore(now) && !item.isDone
        }
    }
}