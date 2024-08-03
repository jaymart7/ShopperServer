package ph.mart.shopper.db.product

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

internal object ProductTable : IntIdTable("products") {
    val title = varchar("title", 255)
}

internal class ProductDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(ProductTable)

    var title by ProductTable.title
}

internal fun daoToProductModel(dao: ProductDAO) = ProductModel(
    id = dao.id.value,
    title = dao.title
)