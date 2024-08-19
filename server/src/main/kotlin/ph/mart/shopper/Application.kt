package ph.mart.shopper

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
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
import ph.mart.shopper.db.configureLocalDatabase
import ph.mart.shopper.db.configureRemoteDatabase
import ph.mart.shopper.model.JwtConfig
import ph.mart.shopper.model.response.ApiErrorResponse
import ph.mart.shopper.routing.configureRouting

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val isLocal = false

    install(CORS) {
        if (isLocal) {
            allowHost("localhost:8081", listOf("http", "https"))
        } else {
            allowHost("jaymart7.github.io", listOf("http", "https"))
        }
        allowHeaders { true }
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
    }

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()
    val jwtConfig = JwtConfig(
        secret = secret,
        issuer = issuer,
        audience = audience,
        realm = myRealm
    )

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

    install(ContentNegotiation) { json() }

    configureRouting(jwtConfig)

    if (isLocal) {
        configureLocalDatabase()
    } else {
        configureRemoteDatabase()
    }
}