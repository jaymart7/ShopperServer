package ph.mart.shopper.db.product

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

internal object ProductTable : IntIdTable("products") {
    val title = varchar("title", 255)
    val dateTime = datetime("date_time")
}

internal class ProductDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(ProductTable)

    var title by ProductTable.title
    var dateTime by ProductTable.dateTime
}

internal fun daoToProductModel(dao: ProductDAO) = ProductModel(
    id = dao.id.value,
    title = dao.title,
    dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
)