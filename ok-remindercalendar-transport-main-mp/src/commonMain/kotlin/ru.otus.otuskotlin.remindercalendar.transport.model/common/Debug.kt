package ru.otus.otuskotlin.remindercalendar.transport.model.common

import kotlinx.serialization.Serializable

@Serializable
data class Debug (
    val mode: WorkModeDto? = null
)
