package ru.otus.otuskotlin.remindercalendar.repository.sql.schema

import org.jetbrains.exposed.dao.id.UUIDTable

object EventsTable: UUIDTable("events") {

    val name = varchar("name", 256)
    val description = text("description")
    val startSchedule = varchar("startSchedule", 256)
    val frequency = varchar("frequency", 256)
    val userId = varchar("userId", 256)
    val mobile = varchar("mobile", 12)
}
