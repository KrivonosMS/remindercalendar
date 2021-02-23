package ru.otus.otuskotlin.remindercalendar.transport.model.event

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.remindercalendar.transport.model.common.FrequencyDto

@Serializable
data class EventUpdateDto(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val startSchedule: String? = null,
    val userId: String?,
    val frequency: FrequencyDto? = null,
    val username: String? = null,
    val mobile: String? = null,
)
