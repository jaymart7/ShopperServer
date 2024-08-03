package ph.mart.shopper.db.account

import org.jetbrains.exposed.sql.and
import ph.mart.shopper.db.suspendTransaction

internal interface AccountRepository {

    suspend fun login(loginRequest: LoginRequest): AccountModel?

    suspend fun addAccount(accountRequest: AccountRequest)

    suspend fun getAccount(username: String): AccountModel?
}

internal class AccountRepositoryImpl : AccountRepository {

    override suspend fun login(loginRequest: LoginRequest): AccountModel? = suspendTransaction {
        AccountDAO
            .find {
                AccountTable.username eq loginRequest.username and (
                        AccountTable.password eq loginRequest.password)
            }
            .limit(1)
            .map(::daoToAccountModel)
            .firstOrNull()
    }

    override suspend fun addAccount(accountRequest: AccountRequest): Unit = suspendTransaction {
        AccountDAO.new {
            name = accountRequest.name
            username = accountRequest.username
            password = accountRequest.password
        }
    }

    override suspend fun getAccount(username: String): AccountModel? = suspendTransaction {
        AccountDAO
            .find { (AccountTable.username eq username) }
            .limit(1)
            .map(::daoToAccountModel)
            .firstOrNull()
    }
}