package ph.mart.shopper.model.response

import kotlinx.serialization.Serializable

@Serializable
internal data class BaseResponse<T>(
    val data: T?,
    val message: String
)