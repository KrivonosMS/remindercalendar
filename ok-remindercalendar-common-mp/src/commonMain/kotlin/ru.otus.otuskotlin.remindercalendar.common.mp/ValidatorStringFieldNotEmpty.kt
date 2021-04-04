package ru.otus.otuskotlin.remindercalendar.common.mp


class ValidatorStringFieldNotEmpty(
    private val message: String = "Field must not be empty or null"
): Validator<String?> {

    override fun validate(sample: String?): ValidationResult {
        return if (sample.isNullOrBlank()) {
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
