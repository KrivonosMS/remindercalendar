package ru.otus.otuskotlin.remindercalendar.transport.model.logs

import ru.otus.otuskotlin.remindercalendar.transport.model.event.EventDto

data class LogModel(
    val requestEventId: String? = null,
    val requestEvent: EventDto? = null,
    val responseEvent: EventDto? = null,
    val responseEvents: List<EventDto>? = null,
)
