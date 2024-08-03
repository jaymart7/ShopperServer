package ph.mart.shopper.routing

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import ph.mart.shopper.db.product.ProductRepository
import ph.mart.shopper.db.product.ProductRequest
import ph.mart.shopper.db.product.toProductModel

internal fun Routing.productRouting(repository: ProductRepository) {
    route("/products") {
        get {
            val products = repository.getProducts()
            call.respond(products)
        }

        post {
            try {
                val productRequest = call.receive<ProductRequest>()
                val id = repository.addProduct(productRequest)
                call.respond(id)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        put("/{productId}") {
            val productId = call.parameters["productId"]?.toInt()
            val productRequest = call.receive<ProductRequest>()
            if (productId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            val productModel = productRequest.toProductModel(productId)
            if (repository.updateProduct(productModel)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/{productId}") {
            val productId = call.parameters["productId"]?.toInt()
            if (productId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            if (repository.removeProduct(productId)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}