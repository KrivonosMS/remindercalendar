package com.example.controller

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.remindercalendar.transport.model.common.ErrorValueDto
import ru.otus.otuskotlin.remindercalendar.transport.model.common.FrequencyDto
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.common.ResponseStatusDto
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*
import java.time.Instant
import java.time.LocalDateTime

class EventController {
    private val log = LoggerFactory.getLogger(this::class.java)!!

    suspend fun read(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventRead

        val response: Message = ResponseEventRead(
            responseId = "123",
            onRequestId = query.requestId,
            endTime = LocalDateTime.now().toString(),
            status = ResponseStatusDto.SUCCESS,
            event = mockEventDto(),
        )
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
    }

    private fun mockEventDto() = EventDto(
        id = "event-id",
        name = "name",
        description = "description",
        startSchedule = "2021-02-25T13:40:00",
        userId = "userId",
        frequency = FrequencyDto.YEARLY,
        mobile = "+7123456789",
    )

    suspend fun create(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventCreate

        val response: Message = ResponseEventCreate(
            responseId = "123",
            onRequestId = query.requestId,
            endTime = LocalDateTime.now().toString(),
            status = ResponseStatusDto.SUCCESS,
            event = EventDto(
                id = "event-id",
                name = query.event?.name,
                description = query.event?.description,
                startSchedule = query.event?.startSchedule,
                userId = query.event?.userId,
                frequency = query.event?.frequency,
                mobile = query.event?.mobile,
            )
        )
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
    }

    suspend fun update(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventUpdate
        val id = query.event?.id

        val response: Message = if (id != null) {
            ResponseEventUpdate(
                responseId = "123",
                onRequestId = query.requestId,
                endTime = LocalDateTime.now().toString(),
                status = ResponseStatusDto.SUCCESS,
                event = EventDto(
                    id = "event-id",
                    name = query.event?.name,
                    description = query.event?.description,
                    startSchedule = query.event?.startSchedule,
                    userId = query.event?.userId,
                    frequency = query.event?.frequency,
                    mobile = query.event?.mobile,
                )
            )
        } else {
            ResponseEventUpdate(
                responseId = "123",
                onRequestId = query.requestId,
                endTime = LocalDateTime.now().toString(),
                status = ResponseStatusDto.ERROR,
                errors = listOf(
                    ErrorValueDto(
                        code = "code",
                        field = "field",
                        message = "message",
                    ),
                )
            )

        }
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
    }

    suspend fun delete(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventDelete

        val response: Message = ResponseEventDelete(
            responseId = "123",
            onRequestId = query.requestId,
            endTime = LocalDateTime.now().toString(),
            status = ResponseStatusDto.SUCCESS,
            event = mockEventDto(),
            deleted = true,
        )
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
    }

    suspend fun filer(pipelineContext: PipelineContext<Unit, ApplicationCall>) = try {
        val query = pipelineContext.call.receive<Message>() as RequestEventFilter

        val response: Message = ResponseEventFilter(
            responseId = "123",
            onRequestId = query.requestId,
            endTime = LocalDateTime.now().toString(),
            status = ResponseStatusDto.SUCCESS,
            events = listOf(
                mockEventDto(),
                mockEventDto(),
            ),
            count = 100,
        )
        pipelineContext.call.respond(response)
    } catch (e: Exception) {
        log.error("read event chain error", e)
    }
}