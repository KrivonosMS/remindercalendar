package ru.otus.otuskotlin.remindercalendar.pipelines.validation

import ru.otus.otuskotlin.remindercalendar.common.mp.ValidationResult
import ru.otus.otuskotlin.remindercalendar.common.mp.Validator


class DefaultValidationOperation<C, T>(
    private val onBlock: C.() -> T,
    private val validator: Validator<T>,
    private var errorHandler: C.(ValidationResult) -> Unit = {}
): ValidationOperation<C, T> {
    override suspend fun execute(context: C) {
        val value = context.onBlock()
        val res = validator.validate(value)
        context.errorHandler(res)
    }

}
