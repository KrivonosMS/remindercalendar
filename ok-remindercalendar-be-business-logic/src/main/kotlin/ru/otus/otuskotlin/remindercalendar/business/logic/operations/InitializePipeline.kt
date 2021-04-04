package ru.otus.otuskotlin.remindercalendar.business.logic.operations

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.operation

object InitializePipeline : Operation<Context> by operation({
    startIf { status == ContextStatus.NONE }
    execute { status = ContextStatus.RUNNING }
})