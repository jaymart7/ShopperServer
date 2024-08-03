package ph.mart.shopper

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import ph.mart.shopper.db.configureDatabases
import ph.mart.shopper.model.response.ApiErrorResponse
import ph.mart.shopper.routing.configureRouting

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.main() {
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                val apiErrorResponse = ApiErrorResponse(
                    code = HttpStatusCode.Unauthorized.value.toString(),
                    message = "Please re-login"
                )
                call.respond(HttpStatusCode.Unauthorized, apiErrorResponse)
            }
        }

    }
    install(CORS) {
        anyHost()
        allowHeaders { true }
        allowHeader(HttpHeaders.ContentType)
    }

    install(ContentNegotiation) { json() }

    configureRouting()
    configureDatabases()
}