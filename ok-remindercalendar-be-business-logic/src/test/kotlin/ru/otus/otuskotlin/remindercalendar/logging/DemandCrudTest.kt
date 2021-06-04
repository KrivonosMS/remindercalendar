package ru.otus.otuskotlin.remindercalendar.logging

import org.assertj.core.api.Assertions.assertThat
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.*
import runBlockingTest
import java.time.LocalDateTime
import kotlin.test.Test

class EventCrudTest {

    @Test
    fun filter() {
        val givenCrud = EventCrud()
        val givenContext = Context(
            stubCase = StubCase.EVENT_FILTER_SUCCESS,
            requestEventFilter = FilterModel(from = 10, pageSize = 20, FrequencyModel.DAILY),
        )

        runBlockingTest { givenCrud.filter(givenContext) }

        assertThat(givenContext.status).isEqualTo(ContextStatus.SUCCESS)
        assertThat(givenContext.responseEventFilter.size).isEqualTo(1)
        assertThat(givenContext.eventsCount).isEqualTo(100)
        assertThat(givenContext.responseEventFilter[0])
            .isEqualToComparingFieldByField(
                EventModel(
                    id = EventIdModel("test-id"),
                    description = "test-description",
                    name = "name",
                    startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                    frequency = FrequencyModel.DAILY,
                    userId = UserId("test-user-id"),
                    mobile = "test-mobile",
                ),
            )
    }

    @Test
    fun create() {
        val givenCrud = EventCrud()
        val givenContext = Context(
            stubCase = StubCase.EVENT_CREATE_SUCCESS,
            requestEvent = EventModel(
                id = EventIdModel.NONE,
                description = "test-description",
                name = "name",
                startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                frequency = FrequencyModel.DAILY,
                userId = UserId("test-user-id"),
                mobile = "test-mobile",
            ),
        )

        runBlockingTest { givenCrud.create(givenContext) }

        assertThat(givenContext.status).isEqualTo(ContextStatus.SUCCESS)
        assertThat(givenContext.responseEvent)
            .isEqualToComparingFieldByField(
                EventModel(
                    id = EventIdModel("test-id"),
                    description = "test-description",
                    name = "name",
                    startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                    frequency = FrequencyModel.DAILY,
                    userId = UserId("test-user-id"),
                    mobile = "test-mobile",
                ),
            )
    }

    @Test
    fun read() {
        val givenCrud = EventCrud()
        val givenContext = Context(
            stubCase = StubCase.EVENT_READ_SUCCESS,
            requestEventId = EventIdModel("test-id")
        )

        runBlockingTest { givenCrud.read(givenContext) }

        assertThat(givenContext.status).isEqualTo(ContextStatus.SUCCESS)
        assertThat(givenContext.responseEvent)
            .isEqualToComparingFieldByField(
                EventModel(
                    id = EventIdModel("test-id"),
                    description = "test-description",
                    name = "name",
                    startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                    frequency = FrequencyModel.DAILY,
                    userId = UserId("test-user-id"),
                    mobile = "test-mobile",
                ),
            )
    }

    @Test
    fun update() {
        val givenCrud = EventCrud()
        val givenContext = Context(
            stubCase = StubCase.EVENT_UPDATE_SUCCESS,
            requestEvent = EventModel(
                id = EventIdModel("test-id"),
                description = "test-description",
                name = "name",
                startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                frequency = FrequencyModel.DAILY,
                userId = UserId("test-user-id"),
                mobile = "test-mobile",
            ),
            requestEventId = EventIdModel("id")
        )

        runBlockingTest { givenCrud.update(givenContext) }

        assertThat(givenContext.status).isEqualTo(ContextStatus.SUCCESS)
        assertThat(givenContext.responseEvent)
            .isEqualToComparingFieldByField(
                EventModel(
                    id = EventIdModel("test-id"),
                    description = "test-description",
                    name = "name",
                    startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                    frequency = FrequencyModel.DAILY,
                    userId = UserId("test-user-id"),
                    mobile = "test-mobile",
                ),
            )
    }

    @Test
    fun delete() {
        val givenCrud = EventCrud()
        val givenContext = Context(
            stubCase = StubCase.EVENT_DELETE_SUCCESS,
            requestEventId = EventIdModel("test-id"),
        )

        runBlockingTest { givenCrud.delete(givenContext) }

        assertThat(givenContext.status).isEqualTo(ContextStatus.SUCCESS)
        assertThat(givenContext.responseEvent)
            .isEqualToComparingFieldByField(
                EventModel(
                    id = EventIdModel("test-id"),
                    description = "test-description",
                    name = "name",
                    startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                    frequency = FrequencyModel.DAILY,
                    userId = UserId("test-user-id"),
                    mobile = "test-mobile",
                ),
            )
    }
}