package ru.otus.otuskotlin.remindercalendar.common.backend.repositories

class RepositoryIndexException(index: String = "") : RuntimeException("Objects not found by index: $index")
