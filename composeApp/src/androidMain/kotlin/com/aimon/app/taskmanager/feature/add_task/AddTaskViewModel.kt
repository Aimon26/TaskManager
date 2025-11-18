package com.aimon.app.taskmanager.feature.add_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimon.app.domain.task.Priority
import com.aimon.app.domain.task.Status
import com.aimon.app.domain.task.Task
import com.aimon.app.domain.task.repository.TaskRepository
import com.aimon.app.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var editingTaskId: Long? = null

    fun loadTask(taskId: Long) {
        editingTaskId = taskId

        viewModelScope.launch {
            val entity = repository.getTask(taskId).first() ?: return@launch

            _uiState.update { state ->
                state.copy(
                    title = entity.title,
                    description = entity.description ?: "",
                    priority = Priority.valueOf(entity.priority),
                    status = Status.valueOf(entity.status),
                    dueDate = DateTimeUtil.fromEpochMillis(System.currentTimeMillis()),
                    colorHex = entity.colorHex
                )
            }
        }
    }
    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value) }
    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }
    fun onPriorityChange(value: Priority) = _uiState.update { it.copy(priority = value) }
    fun onStatusChange(value: Status) = _uiState.update { it.copy(status = value) }
    fun onDueDateChange(value: LocalDateTime) = _uiState.update { it.copy(dueDate = value) }
    fun onColorChange(value: Long) = _uiState.update { it.copy(colorHex = value) }

    // -----------------------
    // SAVE (ADD or UPDATE)
    // -----------------------
    fun saveTask() {
        if (_uiState.value.title.isBlank()) return

        viewModelScope.launch {
            val now = DateTimeUtil.fromEpochMillis(System.currentTimeMillis())

            val task = Task(
                id = editingTaskId ?: 0,
                title = _uiState.value.title,
                description = _uiState.value.description,
                priority = _uiState.value.priority,
                status = _uiState.value.status,
                dueDate = _uiState.value.dueDate,
                created = now,
                updated = now,
                colorHex = _uiState.value.colorHex
            )

            if (editingTaskId == null) {
                repository.addTask(task)
            } else {
                repository.updateTask(task)
            }

            _uiState.update { it.copy(isSaved = true) }
        }
    }
}

data class UiState(
    var title: String = "",
    var description: String = "",
    var priority: Priority = Priority.MEDIUM,
    var status: Status = Status.TO_DO,
    var dueDate: LocalDateTime = DateTimeUtil.now(),
    var colorHex: Long = 0xFFEDEDED.toLong(),
    var isSaved: Boolean = false
)
