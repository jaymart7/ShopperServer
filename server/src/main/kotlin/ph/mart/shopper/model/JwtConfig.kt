package ph.mart.shopper.model

internal data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
)