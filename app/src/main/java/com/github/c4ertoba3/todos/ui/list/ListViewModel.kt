package com.github.c4ertoba3.todos.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.c4ertoba3.todos.data.TodoRepository
import com.github.c4ertoba3.todos.todo.TodoItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class ListViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    val state: StateFlow<ListScreenState> = combine(
        repository.observeTodos,
        _isLoading
    ) { todos, loading ->
        ListScreenState(todos = todos, isLoading = loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ListScreenState(isLoading = true)
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.refreshFromBackend()
            } catch (e: IOException) {
                _errorEvent.emit("Ошибка сети. Показаны кэшированные данные.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteItem(item: TodoItem) {
        viewModelScope.launch {
            try {
                repository.delete(item.uid)
            } catch (e: IOException) {
                _errorEvent.emit("Не удалось удалить с сервера")
            }
        }
    }
}