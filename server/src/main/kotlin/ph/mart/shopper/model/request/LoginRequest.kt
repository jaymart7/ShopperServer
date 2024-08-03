package ph.mart.shopper.model.request

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequest(
    val username: String,
    val password: String
)