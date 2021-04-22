package ru.otus.otuskotlin.remindercalendar.common.backend.model

import java.util.*

interface ItemIdModel {
    val id: String

    fun asString() = id
    fun asUUID(): UUID = UUID.fromString(id)
}
