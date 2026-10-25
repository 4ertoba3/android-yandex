package com.github.c4ertoba3.todos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.c4ertoba3.todos.di.AppContainer
import com.github.c4ertoba3.todos.ui.edit.EditScreen
import com.github.c4ertoba3.todos.ui.edit.EditViewModel
import com.github.c4ertoba3.todos.ui.list.ListScreen
import com.github.c4ertoba3.todos.ui.list.ListViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    container: AppContainer,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LIST,
        modifier = modifier
    ) {
        composable(Routes.LIST) {
            val viewModel: ListViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ListViewModel(container.repository) as T
                    }
                }
            )

            ListScreen(
                viewModel = viewModel,
                onItemClick = { item ->
                    navController.navigate(Routes.edit(item.uid)) { launchSingleTop = true }
                },
                onAddClick = {
                    navController.navigate(Routes.EDIT_BASE) { launchSingleTop = true }
                }
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

            val editViewModel: EditViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return EditViewModel(container.repository, todoId) as T
                    }
                }
            )

            EditScreen(
                viewModel = editViewModel,
                onSaveComplete = { navController.popBackStack() }
            )
        }
    }
}