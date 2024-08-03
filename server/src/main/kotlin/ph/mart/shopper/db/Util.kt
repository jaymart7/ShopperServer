package ph.mart.shopper.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/shopper",
        user = "postgres",
        password = "timeisgold7"
    )
}