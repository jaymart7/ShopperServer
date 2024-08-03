package ph.mart.shopper.db.task

import kotlinx.serialization.Serializable

enum class Priority {
    Low, Medium, High, Vital
}

@Serializable
data class TaskModel(
    val name: String,
    val description: String,
    val priority: Priority
)