package ru.otus.otuskotlin.remindercalendar.transport.model.common

interface Response {
    val responseId: String?
    val onRequestId: String?
    val startTime: String?
    val endTime: String?
    val errors: List<ErrorValueDto>?
    val status: ResponseStatusDto?
}