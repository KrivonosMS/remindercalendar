package ru.otus.otuskotlin.remindercalendar.common.backend.context

import ru.otus.otuskotlin.remindercalendar.common.backend.model.*
import ru.otus.otuskotlin.remindercalendar.common.backend.repositories.EventRepository
import java.time.LocalDateTime

data class Context(
    var principal: PrincipalModel = PrincipalModel.NONE,
    var useAuth: Boolean = true,
    val permissions: MutableSet<Permission> = mutableSetOf(),

    var status: ContextStatus = ContextStatus.NONE,
    var stubCase: StubCase = StubCase.NONE,
    var workMode: WorkMode = WorkMode.DEFAULT,
    var startTime: LocalDateTime = LocalDateTime.MIN,
    var onRequestId: String = "",

    var requestEventId: ItemIdModel = EventIdModel.NONE,
    var requestEvent: EventModel = EventModel.NONE,
    var responseEvent: EventModel = EventModel.NONE,

    var requestEventFilter: FilterModel = FilterModel.NONE,
    var responseEventFilter: List<EventModel> = listOf(),
    var eventsCount: Int = 0,
    var errors: MutableList<ErrorValueModel> = mutableListOf(),

    var eventRepositoryTest: EventRepository = EventRepository.NONE,
    var eventRepositoryProd: EventRepository = EventRepository.NONE,
    var eventRepository: EventRepository = EventRepository.NONE,
)
