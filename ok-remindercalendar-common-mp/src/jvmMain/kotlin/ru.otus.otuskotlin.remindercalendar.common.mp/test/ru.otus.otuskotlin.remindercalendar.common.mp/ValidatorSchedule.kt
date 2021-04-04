package ru.otus.otuskotlin.remindercalendar.common.mp.test.ru.otus.otuskotlin.remindercalendar.common.mp

import ru.otus.otuskotlin.remindercalendar.common.mp.Error
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidationResult
import ru.otus.otuskotlin.remindercalendar.common.mp.Validator
import java.time.LocalDateTime

class ValidatorSchedule (
    private val message: String = "Schedule time must be given"
): Validator<LocalDateTime> {

    override fun validate(sample: LocalDateTime): ValidationResult {
        return if (sample == LocalDateTime.MIN) {
            ValidationResult(
                errors = listOf(
                    Error(
                        message = message,
                    )
                )
            )
        } else {
            ValidationResult.SUCCESS
        }
    }
}