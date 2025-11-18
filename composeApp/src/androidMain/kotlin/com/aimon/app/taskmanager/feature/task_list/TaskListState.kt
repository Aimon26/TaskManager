package com.aimon.app.taskmanager.feature.task_list

import com.aimon.app.taskmanager.shared.database.TaskEntity

data class TaskListState (
    var loading: Boolean=false,
    val task: List<TaskEntity> = emptyList(),
    var error: String?=null
)