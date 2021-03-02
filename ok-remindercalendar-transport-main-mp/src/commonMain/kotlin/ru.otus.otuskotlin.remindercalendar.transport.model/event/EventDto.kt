package ru.otus.otuskotlin.remindercalendar.transport.model.event

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.remindercalendar.transport.model.common.FrequencyDto
import ru.otus.otuskotlin.remindercalendar.transport.model.common.ItemPermissionDto

@Serializable
data class EventDto(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val startSchedule: String? = null,
    val userId: String? = null,
    val frequency: FrequencyDto? = null,
    val mobile: String? = null,
    val permissions: Set<ItemPermissionDto>? = null,
)