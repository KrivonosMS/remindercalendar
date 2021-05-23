package ru.otus.otuskotlin.remindercalendar.common.backend.model

import java.time.LocalDateTime

data class EventModel(
    override val id: ItemIdModel = EventIdModel.NONE,
    override val name: String = "",
    override val description: String = "",
    override val startSchedule: LocalDateTime = LocalDateTime.MIN,
    override val frequency: FrequencyModel = FrequencyModel.NONE,
    override val userId: UserId = UserId.NONE,
    override val mobile: String = "",
    override val owner: UserModel = UserModel.NONE,
) : ItemModel {
    companion object {
        val NONE = EventModel()
    }
}
