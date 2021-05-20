package ru.otus.otuskotlin.remindercalendar.common.backend.repositories

class RepositoryWrongIdException(id: String): Throwable("Wrong ID in operation: $id")
