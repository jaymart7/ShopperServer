package ph.mart.shopper.model.response

import kotlinx.serialization.Serializable
import ph.mart.shopper.db.account.AccountModel

@Serializable
internal data class AccountResponse(
    val name: String,
    val username: String
)

internal fun AccountModel.toAccountResponse() = AccountResponse(
    username = username,
    name = name
)