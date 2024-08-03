package ph.mart.shopper.db.account

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

internal object AccountTable : IntIdTable("account") {
    val name = varchar("name", 50)
    val username = varchar("username", 50)
    val password = varchar("password", 50)
}

internal class AccountDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AccountDAO>(AccountTable)

    var name by AccountTable.name
    var username by AccountTable.username
    var password by AccountTable.password
}

internal fun daoToAccountModel(dao: AccountDAO) = AccountModel(
    dao.name,
    dao.username,
    dao.password,
)