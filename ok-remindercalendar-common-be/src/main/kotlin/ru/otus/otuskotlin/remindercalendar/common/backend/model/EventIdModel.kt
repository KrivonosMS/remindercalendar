package ru.otus.otuskotlin.remindercalendar.common.backend.model

inline class EventIdModel(
    override val id: String
) : ItemIdModel {
    companion object {
        val NONE = EventIdModel("")
    }
}
