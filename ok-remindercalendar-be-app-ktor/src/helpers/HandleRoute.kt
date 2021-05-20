package ru.otus.otuskotlin.remindercalendar.ktor.helpers

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.PrincipalModel
import ru.otus.otuskotlin.remindercalendar.ktor.jsonConfig
import ru.otus.otuskotlin.remindercalendar.ktor.toModel
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Request
import java.time.LocalDateTime

@OptIn(InternalSerializationApi::class)
suspend inline fun <reified T : Request, reified U : Message> PipelineContext<Unit, ApplicationCall>.handleRoute(
    block: suspend Context.(T?) -> U
) {
    val ctx = Context(
        startTime = LocalDateTime.now(),
    )
    try {
        val query = call.receive<Message>() as T
        ctx.status = ContextStatus.RUNNING
        ctx.principal = call.principal<JWTPrincipal>()?.toModel() ?: PrincipalModel.NONE
        val response = ctx.block(query)
        val respJson = jsonConfig.encodeToString(Message::class.serializer(), response)
        call.respondText(respJson, contentType = ContentType.parse("application/json"))
    } catch (e: Throwable) {
        ctx.status = ContextStatus.FAILING
        ctx.errors.add(e.toModel())
        val response = ctx.block(null)
        val respJson = jsonConfig.encodeToString(Message::class.serializer(), response)
        call.respondText(respJson, contentType = ContentType.parse("application/json"))
    }
}
