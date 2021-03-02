package ru.otus.otuskotlin.remindercalendar.transport.model.common

import kotlinx.serialization.Serializable

@Serializable
enum class ItemPermissionDto {
    READ,
    UPDATE,
    DELETE,
}
