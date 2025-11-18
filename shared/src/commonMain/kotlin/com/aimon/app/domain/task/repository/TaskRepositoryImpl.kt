package com.aimon.app.domain.task.repository
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.aimon.app.domain.task.Task
import com.aimon.app.domain.time.DateTimeUtil
import com.aimon.app.taskmanager.shared.database.TaskDatabase
import com.aimon.app.taskmanager.shared.database.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


class TaskRepositoryImpl  (db: TaskDatabase): TaskRepository{
    private val queries=db.taskManagerQueries
    override suspend fun addTask(task: Task): Long {
        return queries.insertTask(
            task.title,
            task.description,
            task.priority.name,
            task.status.name,
            DateTimeUtil.toEpochMillis(task.dueDate),
            DateTimeUtil.toEpochMillis(task.created),
            DateTimeUtil.toEpochMillis(task.updated),
            task.colorHex).value

    }
    override suspend fun deleteTask(id: Long) {
            queries.deleteTask(id)
    }

    override fun getTask(id: Long): Flow<TaskEntity?> {
        return queries.getById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }


    override fun observeTasks(): Flow<List<TaskEntity>> {
        val query: Query<TaskEntity> = queries.getAll()
        return query.asFlow().mapToList(Dispatchers.IO)
    }

    override fun search(title: String): Flow<List<TaskEntity>> {
        val query: Query<TaskEntity> = queries.searchByTitle(title)
        return query.asFlow().mapToList(Dispatchers.IO)
    }

    override suspend fun updateTask(task: Task) {
        queries.updateTask(task.title,task.description,task.priority.name,task.status.name,
            DateTimeUtil.toEpochMillis(task.dueDate),DateTimeUtil.toEpochMillis(task.updated),task.colorHex, task.id)

    }
}