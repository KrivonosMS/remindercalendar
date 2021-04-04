package ru.otus.otuskotlin.remindercalendar.business.logic.pipelines

import ru.otus.otuskotlin.remindercalendar.business.logic.helpers.validation
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.CompletePipeline
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.InitializePipeline
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.stubs.EventDeleteStub
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorStringFieldNotEmpty
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline

object EventDelete : Operation<Context> by pipeline({
    execute(InitializePipeline)

    execute(EventDeleteStub)

    validation {
        validate<String?> {
            validator(ValidatorStringFieldNotEmpty("You must provide schedule event id"))
            on { requestEventId.id }
        }
    }

    execute(CompletePipeline)
})