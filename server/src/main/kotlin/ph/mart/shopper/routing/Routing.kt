package ph.mart.shopper.routing

import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import ph.mart.shopper.db.account.AccountRepositoryImpl
import ph.mart.shopper.db.product.ProductRepositoryImpl
import ph.mart.shopper.db.task.TaskRepositoryImpl
import ph.mart.shopper.model.JwtConfig

internal fun Application.configureRouting(
    jwtConfig: JwtConfig,
) {

    val accountRepository = AccountRepositoryImpl()
    val productRepository = ProductRepositoryImpl()
    val taskRepository = TaskRepositoryImpl()

    routing {
        route("/") {
            get {
                call.respond("Hello world")
            }
        }
        accountRouting(
            accountRepository = accountRepository,
            jwtConfig = jwtConfig
        )
        productRouting(productRepository)
        taskRouting(taskRepository)
    }
}