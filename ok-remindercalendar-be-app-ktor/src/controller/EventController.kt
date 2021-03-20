package com.example.controller

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.remindercalendar.business.logic.EventCrud
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.StubCase
import ru.otus.otuskotlin.remindercalendar.mapper.*
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.common.ResponseStatusDto
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*

class EventController(
    private val eventCrud: EventCrud
) {
    private val log = LoggerFactory.getLogger(this::class.java)!!

    suspend fun read(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventRead
        val context = Context(stubCase = StubCase.EVENT_READ_SUCCESS)
        context.request(query)
        eventCrud.read(context)
        val response: Message = context.toResponseEventRead().copy(responseId = "123", onRequestId = query.requestId)
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
        pipelineContext.call.respond(
            ResponseEventRead(
                responseId = "123",
                status = ResponseStatusDto.ERROR,
            )
        )
    }

    suspend fun create(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventCreate
        val context = Context(stubCase = StubCase.EVENT_CREATE_SUCCESS)
        context.request(query)
        eventCrud.create(context)
        val response: Message = context.toResponseEventCreate().copy(responseId = "123", onRequestId = query.requestId)
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
        pipelineContext.call.respond(
            ResponseEventCreate(
                responseId = "123",
                status = ResponseStatusDto.ERROR,
            )
        )
    }

    suspend fun update(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventUpdate
        val context = Context(stubCase = StubCase.EVENT_UPDATE_SUCCESS)
        context.request(query)
        eventCrud.update(context)
        val response: Message = context.toResponseEventUpdate().copy(responseId = "123", onRequestId = query.requestId)
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
        pipelineContext.call.respond(
            ResponseEventUpdate(
                responseId = "123",
                status = ResponseStatusDto.ERROR,
            )
        )
    }

    suspend fun delete(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventDelete
        val context = Context(stubCase = StubCase.EVENT_DELETE_SUCCESS)
        context.request(query)
        eventCrud.delete(context)
        val response: Message = context.toResponseEventDelete().copy(responseId = "123", onRequestId = query.requestId)
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
        pipelineContext.call.respond(
            ResponseEventDelete(
                responseId = "123",
                status = ResponseStatusDto.ERROR,
            )
        )
    }

    suspend fun filer(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventFilter
        val context = Context(stubCase = StubCase.EVENT_FILTER_SUCCESS)
        context.requestFilter(query)
        eventCrud.filter(context)
        val response: Message = context.toResponseEventFilter().copy(responseId = "123", onRequestId = query.requestId)
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
        pipelineContext.call.respond(
            ResponseEventFilter(
                responseId = "123",
                status = ResponseStatusDto.ERROR,
            )
        )
    }
}