package ru.otus.otuskotlin.remindercalendar.common.backend.context

import ru.otus.otuskotlin.remindercalendar.common.backend.model.ErrorValueModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventIdModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.FilterModel

data class Context(
    var requestEventId: EventIdModel = EventIdModel.NONE,
    var requestEvent: EventModel = EventModel.NONE,
    var responseEvent: EventModel = EventModel.NONE,

    var requestEventFilter: FilterModel = FilterModel.NONE,
    var responseEventFilter: List<EventModel> = listOf(),
    var eventsCount: Int = 0,
    var errors: List<ErrorValueModel> = listOf(),
)
