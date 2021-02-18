package ru.otus.otuskotlin.remindercalendar.common.mp

class AgeValidator(
    private val min: Int,
    private val max: Int
) : Validator<Int> {
    override fun validate(age: Int) = if (age in min..max) {
        ValidationResult.SUCCESS
    } else {
        ValidationResult(
            listOf(
                Error(message = "You are too young for this service")
            )
        )
    }
}