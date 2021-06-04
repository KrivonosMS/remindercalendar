package ru.otus.otuskotlin.remindercalendar.transport.model.logs

import ru.otus.otuskotlin.remindercalendar.transport.model.common.ErrorValueDto

data class CommonLogModel(
    val messageId: String? = null,
    val messageTime: String? = null,
    val logId: String? = null,
    val source: String? = null,
    val remindercalendar: LogModel? = null,
    val errors: List<ErrorValueDto>? = null,
)
