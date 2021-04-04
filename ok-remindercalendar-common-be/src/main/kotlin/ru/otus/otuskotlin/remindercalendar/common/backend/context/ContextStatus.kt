package ru.otus.otuskotlin.remindercalendar.common.backend.context

enum class ContextStatus {
    NONE,
    RUNNING,
    FINISHING,
    FAILING,
    SUCCESS,
    ERROR;

    val isError: Boolean
        get() = this in setOf(FAILING, ERROR)
}