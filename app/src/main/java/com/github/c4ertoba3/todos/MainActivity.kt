package com.github.c4ertoba3.todos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.c4ertoba3.todos.ui.theme.ToDosTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.c4ertoba3.todos.todo.FileStorage
import com.github.c4ertoba3.todos.todo.TodoItem
import com.github.c4ertoba3.todos.todo.Importance
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val storage = FileStorage(this)

        storage.add(TodoItem(text = "Первое дело"))
        storage.add(TodoItem(text = "Второе дело", importance = Importance.HIGH))

        storage.save()
        storage.load()

        Log.d("TODO_TEST", "Дела: ${storage.todos}")

        //LeakHolder.leaked.add(this)

        //var sum = 0L
        //val start = System.currentTimeMillis()
        //for (i in 0..500_000_000) {
        //    sum += i
        //}
        //val elapsed = System.currentTimeMillis() - start
        //Log.d("TODO_TEST", "Тяжёлый цикл: $elapsed ms, sum=$sum")


        enableEdgeToEdge()
        setContent {
            ToDosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDosTheme {
        Greeting("Android")
    }
}