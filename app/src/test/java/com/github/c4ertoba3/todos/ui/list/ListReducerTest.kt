package com.github.c4ertoba3.todos.ui.list

import com.github.c4ertoba3.todos.todo.TodoItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ListReducerTest {

    @Test
    fun `when delete success, item is removed from state`() {
        val item = TodoItem(text = "Тест")
        val initialState = ListScreenState(todos = listOf(item), isLoading = false)

        val action = ListAction.ItemDeleted(item.uid)
        val newState = listReducer(initialState, action)

        assertEquals(emptyList<TodoItem>(), newState.todos)
    }
}