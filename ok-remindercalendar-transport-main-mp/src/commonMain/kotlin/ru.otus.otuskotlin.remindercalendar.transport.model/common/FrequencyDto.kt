package ru.otus.otuskotlin.remindercalendar.transport.model.common

import kotlinx.serialization.Serializable

@Serializable
enum class FrequencyDto {
    DAILY,
    HOURLY,
    WEEKLY,
    YEARLY,
}
