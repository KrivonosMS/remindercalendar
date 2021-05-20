package ru.otus.otuskotlin.remindercalendar.ktor.configs

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

fun Application.featureAuth(authConfig: AuthConfig) {

    install(Authentication) {
        jwt("auth-jwt") {
            skipWhen { ! authConfig.authOff }
            realm = authConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(authConfig.secret))
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.domain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(authConfig.audience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }


}
