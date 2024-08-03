package ph.mart.shopper.db.account

import kotlinx.serialization.Serializable

@Serializable
internal data class AccountModel(
    val name: String,
    val username: String,
    val password: String
)

@Serializable
internal data class AccountRequest(
    val name: String,
    val username: String,
    val password: String
)

@Serializable
internal data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
internal data class AccountResponse(
    val name: String,
    val username: String
)

internal fun AccountModel.toAccountResponse() = AccountResponse(
    username = username,
    name = name
)