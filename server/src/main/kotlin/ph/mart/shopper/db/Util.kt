package ph.mart.shopper.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun configureLocalDatabase() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/shopper",
        user = "postgres",
        password = "timeisgold7"
    )
}

fun configureRemoteDatabase() {
    Database.connect(
//        "jdbc:postgresql://dpg-cqneck5ds78s739bjc0g-a/shopper_database", // Internal
        "jdbc:postgresql://dpg-cqneck5ds78s739bjc0g-a.singapore-postgres.render.com/shopper_database", // External
        user = "mart",
        password = "RiMu06P27FZw0WW6PdRlamFZyzE7625G"
    )
}