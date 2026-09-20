package com.example.todolistapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.todolistapp.todo.FileStorage
import com.example.todolistapp.todo.Importance
import com.example.todolistapp.todo.TodoItem
import com.example.todolistapp.todo.json
import com.example.todolistapp.todo.parse
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storage = FileStorage(this)

        val firstItem = TodoItem(
            text = "Купить продукты",
            importance = Importance.BASIC
        )

        val secondItem = TodoItem(
            text = "Сделать лабораторную",
            importance = Importance.HIGH,
            // обрезаем наносекунды — иначе круговой тест
            // через Unix-время не даст равенства
            deadline = LocalDateTime.now().plusDays(1).withNano(0)
        )

        storage.add(firstItem)
        storage.add(secondItem)

        // изменение дела через copy()
        storage.add(firstItem.copy(isDone = true))

        storage.save()

        // загрузка в новое хранилище
        val newStorage = FileStorage(this)
        newStorage.load()

        Log.d("TODO_TEST", "Дела: ${newStorage.todos}")

        // круговой тест
        val restored = TodoItem.parse(secondItem.json)
        Log.d("TODO_TEST", "Круговой тест: ${restored == secondItem}")

        // удаление по несуществующему uid — не падает
        Log.d("TODO_TEST", "remove unknown: ${newStorage.remove("nope")}")
    }
}