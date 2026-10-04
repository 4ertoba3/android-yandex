package com.github.c4ertoba3.todos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.c4ertoba3.todos.ui.TodoStore
import com.github.c4ertoba3.todos.ui.edit.EditScreen
import com.github.c4ertoba3.todos.ui.list.ListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    store: TodoStore,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LIST,
        modifier = modifier
    ) {
        composable(Routes.LIST) {
            ListScreen(
                todos = store.todos,
                onItemClick = { item ->
                    navController.navigate(Routes.edit(item.uid)) {
                        launchSingleTop = true
                    }
                },
                onAddClick = {
                    navController.navigate(Routes.EDIT_BASE) {
                        launchSingleTop = true
                    }
                },
                onDelete = { item -> store.remove(item.uid) }
            )
        }

        composable(
            route = Routes.EDIT,
            arguments = listOf(
                navArgument(Routes.ARG_TODO_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString(Routes.ARG_TODO_ID)
            val initialItem = remember(todoId) { store.findById(todoId) }

            EditScreen(
                initialItem = initialItem,
                onSave = { item ->
                    store.add(item)
                    navController.popBackStack()
                }
            )
        }
    }
}