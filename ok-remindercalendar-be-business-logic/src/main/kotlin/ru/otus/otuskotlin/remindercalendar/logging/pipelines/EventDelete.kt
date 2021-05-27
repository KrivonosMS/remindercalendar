package ru.otus.otuskotlin.remindercalendar.logging.pipelines

import ru.otus.otuskotlin.remindercalendar.logging.helpers.validation
import ru.otus.otuskotlin.remindercalendar.logging.operations.CompletePipeline
import ru.otus.otuskotlin.remindercalendar.logging.operations.InitializePipeline
import ru.otus.otuskotlin.remindercalendar.logging.operations.QuerySetWorkMode
import ru.otus.otuskotlin.remindercalendar.logging.operations.stubs.EventDeleteStub
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.ErrorValueModel
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorStringFieldNotEmpty
import ru.otus.otuskotlin.remindercalendar.logging.logger
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline

private val logger = logger(EventDelete::class.java)

object EventDelete : Operation<Context> by pipeline({
    execute(InitializePipeline)

    execute(QuerySetWorkMode)

    execute(EventDeleteStub)

    validation {
        validate<String?> {
            validator(ValidatorStringFieldNotEmpty("You must provide schedule event id"))
            on { requestEventId.id }
        }
    }

    operation {
        startIf { status == ContextStatus.RUNNING }
        execute {
            try {
                logger.doWithErrorLoggingSusp("event-delete") {
                    eventRepository.delete(this)
                    status = ContextStatus.FINISHING
                }
            } catch (t: Throwable) {
                status = ContextStatus.FAILING
                errors.add(
                    ErrorValueModel(
                        code = "demand-repo-delete-error",
                        message = "Внутренняя ошибка, обратитесь к администратору")
                )
            }
        }
    }

    execute(CompletePipeline)
})