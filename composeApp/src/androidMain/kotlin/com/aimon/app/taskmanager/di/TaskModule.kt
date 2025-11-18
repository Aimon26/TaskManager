package com.aimon.app.taskmanager.di

import android.content.Context
import com.aimon.app.data.local.DatabaseDriverFactory
import com.aimon.app.domain.task.repository.TaskRepository
import com.aimon.app.domain.task.repository.TaskRepositoryImpl
import com.aimon.app.taskmanager.shared.database.TaskDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        val driverFactory = DatabaseDriverFactory(context)
        val driver = driverFactory.createDriver()
        return TaskDatabase(driver)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(db)
    }
}