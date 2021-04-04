package ru.otus.otuskotlin.remindercalendar.business.logic.pipelines

import ru.otus.otuskotlin.remindercalendar.business.logic.operations.CompletePipeline
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.InitializePipeline
import ru.otus.otuskotlin.remindercalendar.business.logic.operations.stubs.EventFilterStub
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline

object EventFilter : Operation<Context> by pipeline({
    execute(InitializePipeline)

    execute(EventFilterStub)

//    validate<ValidatorFrequencyValue> {
//        validator(ValidatorScheduleValue("You must provide frequency for schedule event"))
//        on { requestEvent.frequency }
//    }

    execute(CompletePipeline)
})