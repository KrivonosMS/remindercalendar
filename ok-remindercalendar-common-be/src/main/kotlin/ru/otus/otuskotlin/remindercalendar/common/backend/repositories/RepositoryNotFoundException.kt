package ru.otus.otuskotlin.remindercalendar.common.backend.repositories

class RepositoryNotFoundException(id: String): RuntimeException("Object with ID=$id is not found")
