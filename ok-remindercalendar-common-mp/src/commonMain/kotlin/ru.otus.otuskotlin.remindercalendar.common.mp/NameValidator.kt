package ru.otus.otuskotlin.remindercalendar.common.mp

class NameValidator : Validator<String> {
    override fun validate(name: String) = if (name.isEmpty()) {
        ValidationResult(
            errors = listOf(
                Error(message = "Name must not be empty")
            )
        )
    } else {
        ValidationResult.SUCCESS
    }
}
