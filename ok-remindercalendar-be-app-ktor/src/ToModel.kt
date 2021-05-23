package ru.otus.otuskotlin.remindercalendar.ktor

import io.ktor.auth.jwt.*
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.remindercalendar.common.backend.model.PrincipalModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.UserGroups
import ru.otus.otuskotlin.remindercalendar.common.backend.model.UserIdModel

private val logger = LoggerFactory.getLogger("Throwable.toModel")

fun JWTPrincipal.toModel() = PrincipalModel(
    id = payload.getClaim("id")
        ?.asString()
        ?.let { UserIdModel(it) }
        ?: UserIdModel.NONE,
    fname = payload.getClaim("fname")?.asString() ?: "",
    mname = payload.getClaim("mname")?.asString() ?: "",
    lname = payload.getClaim("lname")?.asString() ?: "",
    groups = payload
        .getClaim("groups")
        ?.asList(String::class.java)
        ?.mapNotNull {
            try {
                UserGroups.valueOf(it)
            } catch (e: Throwable) {
                null
            }
        }
        ?: emptyList()
)
