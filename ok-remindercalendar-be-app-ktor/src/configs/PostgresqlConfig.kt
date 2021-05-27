package ru.otus.otuskotlin.remindercalendar.backend.app.ktor.configs

import io.ktor.application.*
import io.ktor.util.*

data class PostgresqlConfig(
    val url: String = "jdbc:postgresql://localhost:5432/remindercalendar",
    val driver: String = "org.postgresql.Driver",
    val user: String = "postgres",
    val password: String = "postgres",
    val printLogs: Boolean = false,
) {
    @OptIn(KtorExperimentalAPI::class)
    constructor(environment: ApplicationEnvironment) : this(
        url = environment.config.property("$PATH.url").getString(),
        driver = environment.config.property("$PATH.driver").getString(),
        user = environment.config.property("$PATH.user").getString(),
        password = environment.config.property("$PATH.password").getString(),
        printLogs = environment.config.property("$PATH.printLogs").getString().toBoolean()
    )

    companion object {
        const val PATH = "remindercalendar.repository.postgresql"
    }
}
