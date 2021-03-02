package ru.otus.otuskotlin.remindercalendar.transport.model.common

import kotlinx.serialization.Serializable

@Serializable
enum class ResponseStatusDto {
    SUCCESS,
    BAD_REQUEST,
    ERROR,
    NOT_FOUND,
}
