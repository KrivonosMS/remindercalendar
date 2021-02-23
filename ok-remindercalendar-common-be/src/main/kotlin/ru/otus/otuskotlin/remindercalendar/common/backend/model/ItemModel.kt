package ru.otus.otuskotlin.remindercalendar.common.backend.model

import java.time.LocalDateTime

interface ItemModel {
    val id: ItemIdModel
    val name: String
    val description: String
    val startSchedule: LocalDateTime
    val frequency: FrequencyModel
    val userId: UserId
    val mobile: String
}