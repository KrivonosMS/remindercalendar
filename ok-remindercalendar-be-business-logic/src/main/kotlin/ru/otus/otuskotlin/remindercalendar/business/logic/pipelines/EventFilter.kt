package ru.otus.otuskotlin.remindercalendar.business.logic.pipelines

import ru.otus.otuskotlin.remindercalendar.business.logic.helpers.validation
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.CompletePipeline
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.InitializePipeline
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.QuerySetWorkMode
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.stubs.EventFilterStub
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.ErrorValueModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.FrequencyModel
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorFrequency
import ru.otus.otuskotlin.remindercalendar.pipelines.operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline

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
                eventRepository.filter(this)
                status = ContextStatus.FINISHING
            } catch (t: Throwable) {
                status = ContextStatus.FAILING
                errors.add(
                    ErrorValueModel(
                        code = "event-repository-filter-error",
                        message = t.message ?: ""
                    )
                )
            }
        }
    }

    execute(CompletePipeline)
})