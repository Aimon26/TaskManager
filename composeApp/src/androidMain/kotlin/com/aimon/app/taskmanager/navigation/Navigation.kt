import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aimon.app.taskmanager.feature.add_task.AddTaskScreen
import com.aimon.app.taskmanager.feature.task_list.TaskScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "taskList"
    ) {
        composable(route = "taskList") {
            TaskScreen(navController)
        }

        composable(
            route = "addTask?taskId={taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L // no task
                }
            )
        ) { backStackEntry ->
            val taskIdArg = backStackEntry.arguments?.getLong("taskId") ?: -1L
            val taskId = if (taskIdArg != -1L) taskIdArg else null

            AddTaskScreen(
                navController = navController,
                taskId = taskId
            )
        }
    }
}
