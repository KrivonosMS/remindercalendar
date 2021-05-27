package ru.otus.otuskotlin.remindercalendar.logging.helpers

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.ErrorValueModel
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidationResult
import ru.otus.otuskotlin.remindercalendar.pipelines.Pipeline
import ru.otus.otuskotlin.remindercalendar.pipelines.validation.ValidationBuilder


fun Pipeline.Builder<Context>.validation(block: ValidationBuilder<Context>.() -> Unit) {
    execute(ValidationBuilder<Context>()
        .apply {
            startIf { status == ContextStatus.RUNNING }
            errorHandler { vr: ValidationResult ->
                if (vr.isSuccess()) return@errorHandler
                val errs = vr.errors.map { ErrorValueModel(message = it.message) }
                errors.addAll(errs)
                status = ContextStatus.FAILING
            }
        }
        .apply(block)
        .build())
}
