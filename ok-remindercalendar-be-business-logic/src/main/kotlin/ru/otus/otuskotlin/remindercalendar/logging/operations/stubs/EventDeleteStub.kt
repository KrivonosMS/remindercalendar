package ru.otus.otuskotlin.remindercalendar.logging.operations.stubs

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.*
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline
import java.time.LocalDateTime

object EventDeleteStub : Operation<Context> by pipeline({
    startIf { stubCase != StubCase.NONE }

    operation {
        startIf { stubCase == StubCase.EVENT_DELETE_SUCCESS }
        execute {
            responseEvent = EventModel(
                id = EventIdModel("test-id"),
                description = "test-description",
                name = "name",
                startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                frequency = FrequencyModel.DAILY,
                userId = UserId("test-user-id"),
                mobile = "test-mobile",
            )
            status = ContextStatus.FINISHING
        }
    }
})