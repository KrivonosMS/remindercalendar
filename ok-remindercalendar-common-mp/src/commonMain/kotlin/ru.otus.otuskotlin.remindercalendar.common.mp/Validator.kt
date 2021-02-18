package ru.otus.otuskotlin.remindercalendar.common.mp

interface Validator<T> {
    fun validate(sample: T): ValidationResult
}