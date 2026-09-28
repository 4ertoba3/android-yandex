package com.github.c4ertoba3.todos

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.c4ertoba3.todos.todo.FileStorage
import com.github.c4ertoba3.todos.ui.edit.EditScreen
import com.github.c4ertoba3.todos.ui.theme.ToDosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Загружаем сохранённое дело (если есть)
        val storage = FileStorage(this)
        storage.load()
        Log.d("TODO_TEST", "Loaded: ${storage.todos}")

        setContent {
            ToDosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EditScreen(
                        initialItem = storage.todos.firstOrNull(),
                        onSave = { item ->
                            storage.add(item)
                            storage.save()
                            Log.d("TODO_TEST", "Saved: $item")
                        }
                    )
                }
            }
        }
    }
}