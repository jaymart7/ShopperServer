package ph.mart.shopper.db.product

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import ph.mart.shopper.db.suspendTransaction

internal interface ProductRepository {

    suspend fun getProducts(): List<ProductModel>

    suspend fun addProduct(productRequest: ProductRequest): Int

    suspend fun removeProduct(productId: Int): Boolean

    suspend fun updateProduct(productModel: ProductModel): Boolean
}

internal class ProductRepositoryImpl : ProductRepository {

    override suspend fun getProducts(): List<ProductModel> = suspendTransaction {
        ProductDAO.all().map(::daoToProductModel)
    }

    override suspend fun addProduct(productRequest: ProductRequest): Int = suspendTransaction {
        ProductDAO.new {
            title = productRequest.title
        }.id.value
    }

    override suspend fun removeProduct(productId: Int): Boolean = suspendTransaction {
        val rowsDeleted = ProductTable.deleteWhere {
            ProductTable.id eq productId
        }
        rowsDeleted == 1
    }

    override suspend fun updateProduct(productModel: ProductModel): Boolean = suspendTransaction {
        val rowsUpdated = ProductTable.update({ ProductTable.id eq productModel.id }) {
            it[title] = productModel.title
        }
        rowsUpdated == 1
    }
}