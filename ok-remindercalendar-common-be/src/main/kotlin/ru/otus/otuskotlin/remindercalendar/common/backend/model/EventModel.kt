package ru.otus.otuskotlin.remindercalendar.common.backend.model

import java.time.LocalDateTime

data class EventModel(
    override val id: ItemIdModel = EventIdModel.NONE,
    override val name: String = "",
    override val description: String = "",
    override val startSchedule: LocalDateTime = LocalDateTime.MIN,
    override val frequency: FrequencyModel = FrequencyModel.DAILY,
    override val userId: UserId = UserId.NONE,
    override val mobile: String = "",
) : ItemModel {
    companion object {
        val NONE = EventModel()
    }
}
