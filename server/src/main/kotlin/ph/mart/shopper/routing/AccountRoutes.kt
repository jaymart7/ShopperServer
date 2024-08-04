package ph.mart.shopper.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ph.mart.shopper.db.account.AccountRepository
import ph.mart.shopper.db.account.AccountRequest
import ph.mart.shopper.db.account.LoginRequest
import ph.mart.shopper.db.account.toAccountResponse
import ph.mart.shopper.model.response.ApiErrorResponse
import java.util.Date

internal fun Routing.accountRouting(accountRepository: AccountRepository) {

    val accountNotFound = ApiErrorResponse(
        code = HttpStatusCode.NotFound.value.toString(),
        message = "Account not found"
    )

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()

    authenticate("auth-jwt") {
        get("/account") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val accountResponse = accountRepository.getAccount(username)?.toAccountResponse()

            if (accountResponse != null) {
                call.respond(HttpStatusCode.OK, accountResponse)
            } else {
                call.respond(HttpStatusCode.NotFound, accountNotFound)
            }
        }
    }

    route("/login") {
        post {
            val loginRequest = call.receive<LoginRequest>()
            val accountSession = accountRepository.login(loginRequest)

            if (accountSession == null) {
                call.respond(HttpStatusCode.NotFound, accountNotFound)
            } else {
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", loginRequest.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(hashMapOf("token" to token))
            }
        }
    }

    route("/register") {
        post {
            val accountRequest = call.receive<AccountRequest>()

            try {
                accountRepository.addAccount(accountRequest)
                call.respond(HttpStatusCode.OK, "Account Successfully created")
            } catch (e: Exception) {
                if (e.message?.contains("username_constraint") == true) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiErrorResponse(
                            code = "USRNM".plus(HttpStatusCode.BadRequest.value),
                            message = "Username already taken"
                        )
                    )
                }
            }
        }
    }
}