package com.aimon.app.taskmanager.feature.add_task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aimon.app.domain.task.Priority
import com.aimon.app.domain.task.Status
import com.aimon.app.domain.time.DateTimeUtil
import com.aimon.app.taskmanager.feature.task_list.TaskListState
import com.aimon.app.taskmanager.feature.task_list.TaskScreen
import com.aimon.app.taskmanager.feature.task_list.TaskViewModel
import com.aimon.app.taskmanager.shared.database.TaskEntity

@Composable
fun AddTaskScreen(
    navController: NavController,
    taskId: Long? = null,
    viewModel: AddTaskViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Load when in edit mode
    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTask(taskId)
        }
    }

    // Navigate back after save
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.saveTask() },
                text = { Text(if (taskId == null) "Save Task" else "Update Task") },
                icon = { Icon(Icons.Default.Check, null) }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(Modifier.height(12.dp))

            Text("Priority", style = MaterialTheme.typography.labelLarge)
            var priorityExpanded by remember { mutableStateOf(false) }

            Box {
                OutlinedButton(
                    onClick = { priorityExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(uiState.priority.toString()) }

                DropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    Priority.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = {
                                viewModel.onPriorityChange(it)
                                priorityExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text("Status", style = MaterialTheme.typography.labelLarge)
            var statusExpanded by remember { mutableStateOf(false) }

            Box {
                OutlinedButton(
                    onClick = { statusExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(uiState.status.toString()) }

                DropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    Status.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = {
                                viewModel.onStatusChange(it)
                                statusExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text("Color", style = MaterialTheme.typography.labelLarge)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                val colors = listOf(
                    0xFFE57373.toLong(),
                    0xFF64B5F6.toLong(),
                    0xFF81C784.toLong(),
                    0xFFFFD54F.toLong(),
                    0xFFBA68C8.toLong(),
                )

                colors.forEach { c ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(c))
                            .border(
                                width = if (uiState.colorHex == c) 3.dp else 1.dp,
                                color = if (uiState.colorHex == c) Color.Black else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable { viewModel.onColorChange(c) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}