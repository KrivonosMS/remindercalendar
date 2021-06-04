package ru.otus.otuskotlin.remindercalendar.logging.operations

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.WorkMode
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.operation

object QuerySetWorkMode : Operation<Context> by operation({
    startIf { status == ContextStatus.RUNNING }
    execute {
        eventRepository = when (workMode) {
            WorkMode.TEST -> eventRepositoryTest
            WorkMode.PROD -> eventRepositoryProd
        }
    }
})
