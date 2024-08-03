package ph.mart.shopper.model.response

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiErrorResponse(
    val code: String,
    override val message: String?
) : Exception()