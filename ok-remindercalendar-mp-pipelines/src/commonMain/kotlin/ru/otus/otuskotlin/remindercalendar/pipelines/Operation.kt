package ru.otus.otuskotlin.remindercalendar.pipelines

interface Operation<T> {
    suspend fun execute(context: T)
}