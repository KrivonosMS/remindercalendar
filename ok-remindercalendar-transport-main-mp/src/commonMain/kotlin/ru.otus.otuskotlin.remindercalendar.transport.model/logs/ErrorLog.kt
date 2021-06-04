package ru.otus.otuskotlin.remindercalendar.transport.model.logs

data class ErrorLog(
    val message: String? = null,
    val stackTrace: String? = null,
)
