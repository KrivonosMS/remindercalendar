package ru.otus.otuskotlin.remindercalendar.repository.sql.events

import ru.otus.otuskotlin.remindercalendar.common.backend.model.*
import java.time.LocalDateTime

data class EventInMemoryDto(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val startSchedule: LocalDateTime? = null,
    val frequency: FrequencyModel? = null,
    val userId: String? = null,
    val mobile: String? = null,
) {
    fun toModel() = EventModel(
        id = id?.let { EventIdModel(it) } ?: EventIdModel.NONE,
        name = name ?: "",
        description = description ?: "",
        startSchedule = startSchedule ?: LocalDateTime.MIN,
        frequency = frequency ?: FrequencyModel.NONE,
        userId = userId?.let { UserId(it) } ?: UserId.NONE,
        mobile = mobile ?: "",
    )

    companion object {

        fun of(model: EventModel) = of(model, model.id.id)

        fun of(model: EventModel, id: String) = EventInMemoryDto(
            id = id.takeIf { it.isNotBlank() },
            name = model.name.takeIf { it.isNotBlank() },
            description = model.description.takeIf { it.isNotBlank() },
            startSchedule = model.startSchedule.takeIf { it != LocalDateTime.MIN },
            frequency = model.frequency.takeIf { it != FrequencyModel.NONE },
            userId = model.userId.id.takeIf { it.isNotBlank() },
            mobile = model.mobile.takeIf { it.isNotBlank() },
        )
    }
}
