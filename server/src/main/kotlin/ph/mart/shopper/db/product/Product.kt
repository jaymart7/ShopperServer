package ph.mart.shopper.db.product

import kotlinx.serialization.Serializable

@Serializable
internal data class ProductModel(
    val id: Int,
    val title: String
)

@Serializable
internal data class ProductRequest(
    val title: String
)

internal fun ProductRequest.toProductModel(id: Int) = ProductModel(
    id = id,
    title = title
)