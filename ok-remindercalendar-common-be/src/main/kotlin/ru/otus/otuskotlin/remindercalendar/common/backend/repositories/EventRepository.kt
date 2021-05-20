package ru.otus.otuskotlin.remindercalendar.common.backend.repositories

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventModel

interface EventRepository {
    suspend fun read(context: Context): EventModel
    suspend fun create(context: Context): EventModel
    suspend fun update(context: Context): EventModel
    suspend fun delete(context: Context): EventModel

    suspend fun filter(context: Context): Collection<EventModel>

    companion object {
        val NONE = object : EventRepository {
            override suspend fun read(context: Context): EventModel {
                TODO("Not yet implemented")
            }

            override suspend fun create(context: Context): EventModel {
                TODO("Not yet implemented")
            }

            override suspend fun update(context: Context): EventModel {
                TODO("Not yet implemented")
            }

            override suspend fun delete(context: Context): EventModel {
                TODO("Not yet implemented")
            }

            override suspend fun filter(context: Context): Collection<EventModel> {
                TODO("Not yet implemented")
            }
        }
    }
}
