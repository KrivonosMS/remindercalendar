package ru.otus.otuskotlin.remindercalendar.ktor.services

import org.slf4j.event.Level
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.logging.EventCrud
import ru.otus.otuskotlin.remindercalendar.logging.logger
import ru.otus.otuskotlin.remindercalendar.mapper.*
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*

private val logger = logger(EventService::class.java)

class EventService(private val crud: EventCrud) {

    suspend fun create(context: Context, query: RequestEventCreate?): ResponseEventCreate = with(context) {
        query?.also { request(it) }
        logger.log(
            msg = "Request got",
            level = Level.INFO,
            data = toLog("event-create-request-got")
        )
        crud.create(this)
        logger.log(
            msg = "Response ready ",
            level = Level.INFO,
            data = toLog("event-create-request-handled")
        )
        return toResponseEventCreate()
    }

    suspend fun read(context: Context, query: RequestEventRead?): ResponseEventRead = with(context) {
        query?.also { request(it) }
        logger.log(
            msg = "Request got",
            level = Level.INFO,
            data = toLog("event-read-request-got")
        )
        crud.read(this)
        logger.log(
            msg = "Response ready",
            level = Level.INFO,
            data = toLog("event-read-request-handled")
        )
        return toResponseEventRead()
    }

    suspend fun update(context: Context, query: RequestEventUpdate?): ResponseEventUpdate = with(context) {
        query?.also { request(it) }
        logger.log(
            msg = "Request got",
            level = Level.INFO,
            data = toLog("event-update-request-got")
        )
        crud.update(this)
        logger.log(
            msg = "Response ready",
            level = Level.INFO,
            data = toLog("event-update-update-handled")
        )
        return toResponseEventUpdate()
    }

    suspend fun delete(context: Context, query: RequestEventDelete?): ResponseEventDelete = with(context) {
        query?.also { request(it) }
        logger.log(
            msg = "Request got",
            level = Level.INFO,
            data = toLog("event-delete-request-got")
        )
        crud.delete(this)
        logger.log(
            msg = "Response ready",
            level = Level.INFO,
            data = toLog("event-delete-update-handled")
        )
        return toResponseEventDelete()
    }

    suspend fun filter(context: Context, query: RequestEventFilter?): ResponseEventFilter = with(context) {
        query?.also { requestFilter(it) }
        logger.log(
            msg = "Request got",
            level = Level.INFO,
            data = toLog("event-filter-request-got")
        )
        crud.filter(this)
        logger.log(
            msg = "Response ready",
            level = Level.INFO,
            data = toLog("event-filter-update-handled")
        )
        return toResponseEventFilter()
    }
}
