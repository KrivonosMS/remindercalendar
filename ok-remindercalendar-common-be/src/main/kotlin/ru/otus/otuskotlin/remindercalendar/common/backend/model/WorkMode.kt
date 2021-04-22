package ru.otus.otuskotlin.remindercalendar.common.backend.model

enum class WorkMode {
    PROD,
    TEST;

    companion object {
        val DEFAULT = PROD
    }
}
