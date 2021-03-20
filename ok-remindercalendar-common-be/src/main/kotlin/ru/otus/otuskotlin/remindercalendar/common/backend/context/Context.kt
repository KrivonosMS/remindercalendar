package ru.otus.otuskotlin.remindercalendar.common.backend.context

import ru.otus.otuskotlin.remindercalendar.common.backend.model.*

data class Context(
    var status: ContextStatus = ContextStatus.NONE,
    var stubCase: StubCase = StubCase.NONE,

    var requestEventId: EventIdModel = EventIdModel.NONE,
    var requestEvent: EventModel = EventModel.NONE,
    var responseEvent: EventModel = EventModel.NONE,

    var requestEventFilter: FilterModel = FilterModel.NONE,
    var responseEventFilter: List<EventModel> = listOf(),
    var eventsCount: Int = 0,
    var errors: MutableList<ErrorValueModel> = mutableListOf(),
)
