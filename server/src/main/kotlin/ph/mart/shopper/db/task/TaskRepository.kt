package ph.mart.shopper.db.task


import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import ph.mart.shopper.db.suspendTransaction

internal interface TaskRepository {
    suspend fun allTasks(): List<TaskModel>
    suspend fun tasksByPriority(priority: Priority): List<TaskModel>
    suspend fun taskByName(name: String): TaskModel?
    suspend fun addTask(taskModel: TaskModel)
    suspend fun removeTask(name: String): Boolean
}

internal class TaskRepositoryImpl : TaskRepository {
    override suspend fun allTasks(): List<TaskModel> = suspendTransaction {
        TaskDAO.all().map(::daoToModel)
    }

    override suspend fun tasksByPriority(priority: Priority): List<TaskModel> = suspendTransaction {
        TaskDAO
            .find { (TaskTable.priority eq priority.toString()) }
            .map(::daoToModel)
    }

    override suspend fun taskByName(name: String): TaskModel? = suspendTransaction {
        TaskDAO
            .find { (TaskTable.name eq name) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun addTask(taskModel: TaskModel): Unit = suspendTransaction {
        TaskDAO.new {
            name = taskModel.name
            description = taskModel.description
            priority = taskModel.priority.toString()
        }
    }

    override suspend fun removeTask(name: String): Boolean = suspendTransaction {
        val rowsDeleted = TaskTable.deleteWhere {
            TaskTable.name eq name
        }
        rowsDeleted == 1
    }
}