package ru.otus.otuskotlin.remindercalendar.common.mp.test.ru.otus.otuskotlin.remindercalendar.common.mp

import ru.otus.otuskotlin.remindercalendar.common.backend.model.FrequencyModel
import ru.otus.otuskotlin.remindercalendar.common.mp.Error
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidationResult
import ru.otus.otuskotlin.remindercalendar.common.mp.Validator

class ValidatorFrequency(
    private val message: String = "Frequency period must be given"
) : Validator<FrequencyModel> {

    override fun validate(sample: FrequencyModel): ValidationResult {
        return if (sample == FrequencyModel.NONE) {
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