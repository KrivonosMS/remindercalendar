package ru.otus.otuskotlin.remindercalendar.transport.model.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Debug
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Request

@Serializable
@SerialName("RequestEventFilter")
data class RequestEventFilter(
    override val requestId: String? = null,
    override val onResponseId: String? = null,
    override val startTime: String? = null,
    override val debug: Debug? = null,
    val filter: EventFilterDto? = null,
): Request, Message()
