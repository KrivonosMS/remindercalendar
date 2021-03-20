package ru.otus.otuskotlin.remindercalendar.business.logic

import ru.otus.otuskotlin.remindercalendar.business.logic.pipelines.EventCreate
import ru.otus.otuskotlin.remindercalendar.business.logic.pipelines.EventDelete
import ru.otus.otuskotlin.remindercalendar.business.logic.pipelines.EventFilter
import ru.otus.otuskotlin.remindercalendar.business.logic.pipelines.EventRead
import ru.otus.otuskotlin.remindercalendar.business.logic.pipelines.EventUpdate
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context

class EventCrud {
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

    }
}