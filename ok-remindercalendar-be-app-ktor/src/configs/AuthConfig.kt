package ru.otus.otuskotlin.remindercalendar.ktor.configs

import io.ktor.application.*
import io.ktor.util.*

data class AuthConfig(
    val secret: String,
    val audience: String,
    val domain: String,
    val realm: String,
    val authOff: Boolean = false,
) {
    @OptIn(KtorExperimentalAPI::class)
    constructor(environment: ApplicationEnvironment, authOff: Boolean = false) : this(
        secret = environment.config.propertyOrNull("$PATH.secret")
            ?.getString()
            ?: "remindercalendar-secret",
        audience = environment.config.propertyOrNull("$PATH.audience")
            ?.getString()
            ?: "remindercalendar-users",
        domain = environment.config.propertyOrNull("$PATH.domain")
            ?.getString()
            ?: "http://localhost/",
        realm = environment.config.propertyOrNull("$PATH.realm")
            ?.getString()
            ?: "Remindercalendar",
    )

    companion object {
        const val PATH = "remindercalendar.auth.jwt"
    }
}
