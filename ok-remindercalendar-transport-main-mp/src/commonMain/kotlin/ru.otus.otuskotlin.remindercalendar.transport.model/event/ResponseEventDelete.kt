package ru.otus.otuskotlin.remindercalendar.transport.model.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.remindercalendar.transport.model.common.*

@Serializable
@SerialName("ResponseEventDelete")
data class ResponseEventDelete(
    override val responseId: String? = null,
    override val onRequestId: String? = null,
    override val startTime: String? = null,
    override val endTime: String? = null,
    override val errors: List<ErrorValueDto>? = null,
    override val status: ResponseStatusDto? = null,
    val event: EventDto? = null,
    val deleted: Boolean? = null,
): Response, Message()
