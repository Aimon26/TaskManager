package com.aimon.app.data.local

import app.cash.sqldelight.db.SqlDriver
import com.aimon.app.taskmanager.shared.database.TaskDatabase

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}