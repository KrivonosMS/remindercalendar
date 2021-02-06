package ru.otus.otuskotlin.remindercalendar.common.mp

class ValidationResult(val errors: List<Error>) {

    constructor(vararg results: ValidationResult) : this(
        errors = results.flatMap { it.errors }.toList()
    ) {

    }

    fun isSuccess() = errors.isEmpty()

    companion object {
        val SUCCESS = ValidationResult(emptyList())
    }
}