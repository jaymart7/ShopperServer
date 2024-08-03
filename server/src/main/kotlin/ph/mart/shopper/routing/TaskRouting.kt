package ph.mart.shopper.routing

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ph.mart.shopper.db.task.Priority
import ph.mart.shopper.db.task.TaskModel
import ph.mart.shopper.db.task.TaskRepository

internal fun Routing.taskRouting(repository: TaskRepository) {
    route("/tasks") {
        get {
            val tasks = repository.allTasks()
            call.respond(tasks)
        }

        get("/byName/{taskName}") {
            val name = call.parameters["taskName"]
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val task = repository.taskByName(name)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(task)
        }

        get("/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            if (priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = repository.tasksByPriority(priority)


                if (tasks.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(tasks)
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post {
            try {
                val taskModel = call.receive<TaskModel>()
                repository.addTask(taskModel)
                call.respond(HttpStatusCode.NoContent)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/{taskName}") {
            val name = call.parameters["taskName"]
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            if (repository.removeTask(name)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}