package com.github.c4ertoba3.todos.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.c4ertoba3.todos.data.TodoRepository
import com.github.c4ertoba3.todos.todo.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditViewModel(
    private val repository: TodoRepository,
    private val todoId: String?
) : ViewModel() {

    private val _initialItem = MutableStateFlow<TodoItem?>(null)
    val initialItem: StateFlow<TodoItem?> = _initialItem.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        if (todoId != null) {
            _isLoading.value = true
            viewModelScope.launch {
                _initialItem.value = repository.getTodo(todoId)
                _isLoading.value = false
            }
        }
    }

    fun save(item: TodoItem) {
        viewModelScope.launch {
            repository.save(item)
        }
    }
}