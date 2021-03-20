package ru.otus.otuskotlin.remindercalendar.pipelines.validation

import ru.otus.otuskotlin.remindercalendar.common.mp.ValidationResult
import ru.otus.otuskotlin.remindercalendar.common.mp.Validator
import ru.otus.otuskotlin.remindercalendar.pipelines.PipelineDsl

@PipelineDsl
class ValidationOperationBuilder<C, T>(
    private var errorHandler: C.(ValidationResult) -> Unit = {}
) {
    private lateinit var onBlock: C.() -> T
    private lateinit var validator: Validator<T>
    fun validator(validator: Validator<T>) {
        this.validator = validator
    }

    fun on(block: C.() -> T) {
        onBlock = block
    }

    fun build(): ValidationOperation<C, T> {
        return DefaultValidationOperation(
            validator = validator,
            onBlock = onBlock,
            errorHandler = errorHandler
        )
    }
}
