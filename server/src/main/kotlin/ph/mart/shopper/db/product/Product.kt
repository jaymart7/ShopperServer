package ph.mart.shopper.db.product

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductModel(
    val id: Int,
    val title: String,
    val dateTime: LocalDateTime,
)

@Serializable
internal data class ProductRequest(
    val title: String,
    val dateTime: LocalDateTime
)

internal fun ProductRequest.toProductModel(id: Int) = ProductModel(
    id = id,
    title = title,
    dateTime = dateTime
)