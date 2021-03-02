package ru.otus.otuskotlin.remindercalendar.common.backend.model

inline class UserId(
    val id: String
) {
    companion object {
        val NONE = UserId("")
    }
}