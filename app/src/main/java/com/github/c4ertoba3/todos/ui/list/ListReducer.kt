package com.github.c4ertoba3.todos.ui.list

fun listReducer(state: ListScreenState, action: ListAction): ListScreenState {
    return when (action) {
        is ListAction.LoadStarted -> state.copy(
            isLoading = true,
            error = null,
            latestRequestId = action.requestId
        )

        is ListAction.LoadSuccess -> {
            if (action.requestId < state.latestRequestId) {
                state
            } else {
                state.copy(
                    isLoading = false,
                    todos = action.todos,
                    error = null
                )
            }
        }

        is ListAction.LoadError -> {
            if (action.requestId < state.latestRequestId) {
                state
            } else {
                state.copy(isLoading = false, error = action.message)
            }
        }

        is ListAction.DeleteItem -> state

        is ListAction.ItemDeleted -> state.copy(
            todos = state.todos.filterNot { it.uid == action.uid }
        )
    }
}