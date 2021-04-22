package ru.otus.otuskotlin.remindercalendar.repository.sql.schema

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.otus.otuskotlin.remindercalendar.common.backend.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EventDto(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by EventsTable.name
    var description by EventsTable.description
    var startSchedule by EventsTable.startSchedule
    var frequency by EventsTable.frequency
    var userId by EventsTable.userId
    var mobile by EventsTable.mobile

    fun toModel() = EventModel(
        id = EventIdModel(id.value.toString()),
        name = name?: "",
        description = description?: "",
        startSchedule = startSchedule?.let { LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) },
        frequency = FrequencyModel.valueOf(frequency),
        userId = userId?.let { UserId(userId) } ?: UserId.NONE,
        mobile = mobile?: "",
    )

    fun of(model: EventModel) {
        name = model.name
        description = model.description
        startSchedule = model.startSchedule.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        frequency = model.frequency.toString()
        userId =  model.userId.id
        mobile = model.mobile
    }

    companion object : UUIDEntityClass<EventDto>(EventsTable) {
    }
}
