package ru.otus.otuskotlin.remindercalendar.logging.operations

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline

object CompletePipeline : Operation<Context> by pipeline({
    operation {
        startIf { status in setOf(ContextStatus.RUNNING, ContextStatus.FINISHING) }
        execute { status = ContextStatus.SUCCESS }
    }
    operation {
        startIf { status != ContextStatus.SUCCESS }
        execute { status = ContextStatus.ERROR }
    }
})