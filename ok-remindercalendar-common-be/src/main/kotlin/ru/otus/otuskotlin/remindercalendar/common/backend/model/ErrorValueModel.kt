package ru.otus.otuskotlin.remindercalendar.common.backend.model

data class ErrorValueModel (
    val code: String = "",
    val field: String = "",
    val message: String = "",
)
