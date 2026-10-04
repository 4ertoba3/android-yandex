package com.github.c4ertoba3.todos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.github.c4ertoba3.todos.navigation.AppNavHost
import com.github.c4ertoba3.todos.todo.FileStorage
import com.github.c4ertoba3.todos.ui.theme.ToDosTheme
import com.github.c4ertoba3.todos.ui.TodoStore
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val storage = FileStorage(this)
        storage.load()

        setContent {
            ToDosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val store = remember { TodoStore(storage) }
                    val navController = rememberNavController()

                    AppNavHost(
                        navController = navController,
                        store = store
                    )
                }
            }
        }
    }
}