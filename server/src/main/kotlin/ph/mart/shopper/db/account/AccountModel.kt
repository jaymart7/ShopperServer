package ph.mart.shopper.db.account

import kotlinx.serialization.Serializable

@Serializable
internal data class AccountModel(
    val name: String,
    val username: String,
    val password: String
)