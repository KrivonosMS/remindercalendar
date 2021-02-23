package ru.otus.otuskotlin.remindercalendar.transport.model.common

import kotlinx.serialization.Serializable

@Serializable
data class ErrorValueDto(
    val code: String? = null,
    val field: String? = null,
    val message: String? = null,
)
