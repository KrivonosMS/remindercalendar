package ru.otus.otuskotlin.remindercalendar.transport.model.event

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.remindercalendar.transport.model.common.FrequencyDto

@Serializable
data class EventFilterDto(
    val frequency: FrequencyDto? = null,
    val from: Int? = null,
    val pageSize: Int? = null,
)
