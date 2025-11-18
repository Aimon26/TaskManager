package com.aimon.app.taskmanager.feature.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimon.app.domain.task.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(TaskListState())
    val uiState: StateFlow<TaskListState> = _uiState.asStateFlow()

    private var tasksJob: Job? = null

    init {
        loadTasks()
    }

    fun loadTasks() {
        tasksJob?.cancel()
        tasksJob = viewModelScope.launch {
            repository.observeTasks().collect { tasks ->
                _uiState.update { it.copy(task = tasks, loading = false) }
            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    fun searchTasks(query: String) {
        tasksJob?.cancel()
        tasksJob = viewModelScope.launch {
            repository.search(query).collect { tasks ->
                _uiState.value = _uiState.value.copy(task = tasks)
            }
        }
    }
}