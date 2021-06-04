package ru.otus.otuskotlin.remindercalendar.mapper

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventIdModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventModel
import ru.otus.otuskotlin.remindercalendar.transport.model.logs.CommonLogModel
import ru.otus.otuskotlin.remindercalendar.transport.model.logs.LogModel
import java.time.Instant
import java.util.*

fun Context.toLog(logId: String) = CommonLogModel(
    messageId = UUID.randomUUID().toString(),
    messageTime = Instant.now().toString(),
    source = "ok-remindercalendar",
    logId = logId,

    remindercalendar = LogModel(
        requestEventId = requestEventId.takeIf { it != EventIdModel.NONE }?.asString(),
        requestEvent = requestEvent.takeIf { it != EventModel.NONE }?.toEventDto(),
        responseEvent = responseEvent.takeIf { it != EventModel.NONE }?.toEventDto(),
        responseEvents = responseEventFilter.takeIf { it.isNotEmpty() }?.map { it.toEventDto() },
    ),

    errors = errors.toErrorValueDtoList()

)
