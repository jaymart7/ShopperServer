package ph.mart.shopper.routing

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import ph.mart.shopper.db.account.AccountRepositoryImpl
import ph.mart.shopper.db.product.ProductRepositoryImpl
import ph.mart.shopper.db.task.TaskRepositoryImpl

internal fun Application.configureRouting() {

    val accountRepository = AccountRepositoryImpl()
    val productRepository = ProductRepositoryImpl()
    val taskRepository = TaskRepositoryImpl()

    routing {
        accountRouting(accountRepository)
        productRouting(productRepository)
        taskRouting(taskRepository)
    }
}