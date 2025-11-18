package com.aimon.app.domain.task.repository

import com.aimon.app.domain.task.Task
import com.aimon.app.taskmanager.shared.database.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(): Flow<List<TaskEntity>>
    fun getTask(id: Long): Flow<TaskEntity?>
    suspend fun addTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: Long)
    fun search(title: String): Flow<List<TaskEntity>>
}