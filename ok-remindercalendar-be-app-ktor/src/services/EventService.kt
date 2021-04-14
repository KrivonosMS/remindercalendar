package ru.otus.otuskotlin.remindercalendar.ktor.services

import ru.otus.otuskotlin.remindercalendar.business.logic.EventCrud
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.mapper.*
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*

class EventService(private val crud: EventCrud) {

    suspend fun create(context: Context, query: RequestEventCreate?): ResponseEventCreate = with(context) {
        query?.also { request(it) }
        crud.create(this)
        return toResponseEventCreate()
    }

    suspend fun read(context: Context, query: RequestEventRead?): ResponseEventRead = with(context) {
        query?.also { request(it) }
        crud.read(this)
        return toResponseEventRead()
    }

    suspend fun update(context: Context, query: RequestEventUpdate?): ResponseEventUpdate = with(context) {
        query?.also { request(it) }
        crud.update(this)
        return toResponseEventUpdate()
    }

    suspend fun delete(context: Context, query: RequestEventDelete?): ResponseEventDelete = with(context) {
        query?.also { request(it) }
        crud.delete(this)
        return toResponseEventDelete()
    }

    suspend fun filter(context: Context, query: RequestEventFilter?): ResponseEventFilter = with(context) {
        query?.also { requestFilter(it) }
        crud.filter(this)
        return toResponseEventFilter()
    }
}
