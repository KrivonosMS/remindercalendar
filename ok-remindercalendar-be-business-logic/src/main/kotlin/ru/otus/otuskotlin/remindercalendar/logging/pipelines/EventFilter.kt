package ru.otus.otuskotlin.remindercalendar.logging.pipelines

import ru.otus.otuskotlin.remindercalendar.logging.helpers.validation
import ru.otus.otuskotlin.remindercalendar.logging.operations.CompletePipeline
import ru.otus.otuskotlin.remindercalendar.logging.operations.InitializePipeline
import ru.otus.otuskotlin.remindercalendar.logging.operations.QuerySetWorkMode
import ru.otus.otuskotlin.remindercalendar.logging.operations.stubs.EventFilterStub
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.ErrorValueModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.FrequencyModel
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorFrequency
import ru.otus.otuskotlin.remindercalendar.logging.logger
import ru.otus.otuskotlin.remindercalendar.pipelines.operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline

private val logger = logger(EventFilter::class.java)

object EventFilter : Operation<Context> by pipeline({
    execute(InitializePipeline)

    execute(QuerySetWorkMode)

    execute(EventFilterStub)

    validation {
        validate<FrequencyModel> {
            validator(ValidatorFrequency("You must provide frequency for schedule event filter"))
            on { requestEventFilter.frequencyModel }
        }
    }

    operation {
        startIf { status == ContextStatus.RUNNING }
        execute {
            try {
                logger.doWithErrorLoggingSusp("event-filter") {
                    eventRepository.filter(this)
                    status = ContextStatus.FINISHING
                }
            } catch (t: Throwable) {
                status = ContextStatus.FAILING
                errors.add(
                    ErrorValueModel(
                        code = "event-repository-filter-error",
                        message = "Внутренняя ошибка, обратитесь к администратору"
                    )
                )
            }
        }
    }

    execute(CompletePipeline)
})