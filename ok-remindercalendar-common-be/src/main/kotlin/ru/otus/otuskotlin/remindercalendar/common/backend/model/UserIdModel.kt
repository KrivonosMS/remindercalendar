package ru.otus.otuskotlin.remindercalendar.common.backend.model

inline class UserIdModel(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = UserIdModel("")
    }
}
