package com.github.c4ertoba3.todos.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.c4ertoba3.todos.di.AppContainer
import com.github.c4ertoba3.todos.ui.edit.EditScreen
import com.github.c4ertoba3.todos.ui.list.*
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController,
    container: AppContainer,
    modifier: Modifier = Modifier
) {
    var listState by remember { mutableStateOf(ListScreenState()) }

    var currentRequestId by remember { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()

    lateinit var handleAction: (ListAction) -> Unit

    handleAction = { action ->
        listState = listReducer(listState, action)

        when (action) {
            is ListAction.LoadStarted -> {
                scope.launch {
                    try {
                        val todos = container.todoService.loadTodos()
                        handleAction(ListAction.LoadSuccess(action.requestId, todos))
                    } catch (e: Exception) {
                        handleAction(ListAction.LoadError(action.requestId, e.message ?: "Ошибка"))
                    }
                }
            }
            is ListAction.DeleteItem -> {
                scope.launch {
                    val success = container.todoService.deleteTodo(action.item.uid)
                    if (success) {
                        handleAction(ListAction.ItemDeleted(action.item.uid))
                    }
                }
            }
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        currentRequestId++
        handleAction(ListAction.LoadStarted(currentRequestId))
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LIST,
        modifier = modifier
    ) {
        composable(Routes.LIST) {
            ListScreen(
                state = listState,
                onAction = handleAction,
                onItemClick = { item -> navController.navigate(Routes.edit(item.uid)) { launchSingleTop = true } },
                onAddClick = { navController.navigate(Routes.EDIT_BASE) { launchSingleTop = true } }
            )
        }

        composable(Routes.EDIT, arguments = listOf(navArgument(Routes.ARG_TODO_ID) { type = NavType.StringType; nullable = true; defaultValue = null })) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString(Routes.ARG_TODO_ID)
            val initialItem = listState.todos.find { it.uid == todoId }

            EditScreen(
                initialItem = initialItem,
                onSave = { item ->
                    scope.launch {
                        container.todoService.addOrUpdateTodo(item)
                        currentRequestId++
                        handleAction(ListAction.LoadStarted(currentRequestId))
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}