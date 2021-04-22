package ru.otus.otuskotlin.remindercalendar.business.logic.pipelines

import ru.otus.otuskotlin.remindercalendar.business.logic.helpers.validation
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.CompletePipeline
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.InitializePipeline
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.QuerySetWorkMode
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.stubs.EventReadStub
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.ErrorValueModel
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorStringFieldNotEmpty
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline

object EventRead : Operation<Context> by pipeline({
    execute(InitializePipeline)

    execute(QuerySetWorkMode)

    execute(EventReadStub)

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
                eventRepository.read(this)
                status = ContextStatus.FINISHING
            } catch (t: Throwable) {
                status = ContextStatus.FAILING
                errors.add(
                    ErrorValueModel(
                        code = "event-repository-read-error",
                        message = t.message ?: ""
                    )
                )
            }
        }
    }

    execute(CompletePipeline)
})