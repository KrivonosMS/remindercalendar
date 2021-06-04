package ru.otus.otuskotlin.remindercalendar.logging

import ru.otus.otuskotlin.remindercalendar.logging.pipelines.EventCreate
import ru.otus.otuskotlin.remindercalendar.logging.pipelines.EventDelete
import ru.otus.otuskotlin.remindercalendar.logging.pipelines.EventFilter
import ru.otus.otuskotlin.remindercalendar.logging.pipelines.EventRead
import ru.otus.otuskotlin.remindercalendar.logging.pipelines.EventUpdate
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.repositories.EventRepository

class EventCrud(
    private val eventRepositoryProd: EventRepository = EventRepository.NONE,
    private val eventRepositoryTest: EventRepository = EventRepository.NONE,
) {
    suspend fun filter(context: Context) {
        EventFilter.execute(context.apply(this::configureContext))
    }

    suspend fun create(context: Context) {
        EventCreate.execute(context.apply(this::configureContext))
    }

    suspend fun read(context: Context) {
        EventRead.execute(context.apply(this::configureContext))
    }

    suspend fun update(context: Context) {
        EventUpdate.execute(context.apply(this::configureContext))
    }

    suspend fun delete(context: Context) {
        EventDelete.execute(context.apply(this::configureContext))
    }

    private fun configureContext(context: Context) {
        context.eventRepositoryTest = eventRepositoryTest
        context.eventRepositoryProd = eventRepositoryProd

    }
}