package ru.otus.otuskotlin.remindercalendar.transport.model.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.remindercalendar.transport.model.common.*

@Serializable
@SerialName("ResponseEventFilter")
data class ResponseEventFilter(
    override val responseId: String? = null,
    override val onRequestId: String? =  null,
    override val startTime: String? = null,
    override val endTime: String? =  null,
    override val errors: List<ErrorValueDto>? =  null,
    override val status: ResponseStatusDto? =  null,
    val events: List<EventDto>? =  null,
    val count: Int? = null,
): Response, Message()
