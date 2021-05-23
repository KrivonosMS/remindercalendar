package ru.otus.otuskotlin.remindercalendar.business.logic

import ru.otus.otuskotlin.remindercalendar.business.logic.pipelines.EventRead
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventIdModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.StubCase
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class EventReadAuth {

    @Test
    fun `eventId success non-empty`() {
        val ctx = Context(
            requestEventId = EventIdModel("123"),
            stubCase = StubCase.EVENT_READ_SUCCESS,
            useAuth = true
        )

        runBlockingTest {
            EventRead.execute(ctx)
            assertEquals(ContextStatus.SUCCESS, ctx.status)
            assertEquals(0, ctx.errors.size)
        }
    }

    @Test
    fun `eventId fails empty`() {
        val ctx = Context(
            requestEventId = EventIdModel(""),
            useAuth = false
        )

        runBlockingTest {
            EventRead.execute(ctx)
            assertEquals(ContextStatus.ERROR, ctx.status)
            assertTrue { ctx.errors.first().message == "You must provide schedule event id" }
        }
    }
}
