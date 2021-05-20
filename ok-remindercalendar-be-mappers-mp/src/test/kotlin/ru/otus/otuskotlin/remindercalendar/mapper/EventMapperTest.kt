package ru.otus.otuskotlin.remindercalendar.mapper

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.*
import ru.otus.otuskotlin.remindercalendar.transport.model.common.*
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.test.Test

class EventMapperTest {
    @BeforeEach
    fun mockInit() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.of(2020, 1, 1, 10, 20)
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString("2b5edf5c-0388-4f55-9b05-675460e0462d")
    }

    @AfterEach
    fun destroy() {
        unmockkStatic(LocalDateTime::class)
        unmockkStatic(UUID::class)
    }

    @Test
    fun `set request create event`() {
        val requestEventCreate = RequestEventCreate(
            requestId = "create-id",
            debug = Debug(mode = WorkModeDto.PROD),
            event = EventCreateDto(
                name = "День рождения жены",
                description = "Этот день самый главный в году. Про него никак нельзя забыть",
                startSchedule = "2021-02-25T13:40:00",
                userId = "user-id",
                frequency = FrequencyDto.YEARLY,
                mobile = "+7123456789",

                )
        )
        val context = Context(
            startTime = LocalDateTime.now().minusMinutes(1),
        )

        context.request(requestEventCreate)

        assertThat(context.requestEventId).isEqualTo(EventIdModel("create-id"))
        assertThat(context.requestEvent).isEqualTo(
            EventModel(
                id = EventIdModel.NONE,
                name = "День рождения жены",
                description = "Этот день самый главный в году. Про него никак нельзя забыть",
                startSchedule = LocalDateTime.parse("2021-02-25T13:40:00", DateTimeFormatter.ISO_DATE_TIME),
                frequency = FrequencyModel.YEARLY,
                userId = UserId("user-id"),
                mobile = "+7123456789",
            )
        )
    }

    @Test
    fun `set request update event`() {
        val requestEventUpdate = RequestEventUpdate(
            requestId = "create-id",
            debug = Debug(mode = WorkModeDto.PROD),
            event = EventUpdateDto(
                id = "event-id",
                name = "День рождения жены",
                description = "Этот день самый главный в году. Про него никак нельзя забыть",
                startSchedule = "2021-02-25T13:40:00",
                userId = "user-id",
                frequency = FrequencyDto.YEARLY,
                mobile = "+7123456789",
            )
        )
        val context = Context()

        context.request(requestEventUpdate)

        assertThat(context.requestEventId).isEqualTo(EventIdModel("create-id"))
        assertThat(context.requestEvent).isEqualTo(
            EventModel(
                id = EventIdModel("event-id"),
                name = "День рождения жены",
                description = "Этот день самый главный в году. Про него никак нельзя забыть",
                startSchedule = LocalDateTime.parse("2021-02-25T13:40:00", DateTimeFormatter.ISO_DATE_TIME),
                frequency = FrequencyModel.YEARLY,
                userId = UserId("user-id"),
                mobile = "+7123456789",
            )
        )
    }

    @Test
    fun `set request read event`() {
        val requestEventRead = RequestEventRead(
            requestId = "request-id",
            debug = Debug(mode = WorkModeDto.PROD),
            eventId = "event-id"
        )
        val context = Context()

        context.request(requestEventRead)

        assertThat(context.requestEventId).isEqualTo(EventIdModel("event-id"))
        assertThat(context.requestEvent).isEqualTo(EventModel.NONE)
    }

    @Test
    fun `set request delete event`() {
        val requestEventDelete = RequestEventDelete(
            requestId = "request-id",
            debug = Debug(mode = WorkModeDto.PROD),
            eventId = "event-id"
        )
        val context = Context()

        context.request(requestEventDelete)

        assertThat(context.requestEventId).isEqualTo(EventIdModel("event-id"))
        assertThat(context.requestEvent).isEqualTo(EventModel.NONE)
    }

    @Test
    fun `set request event filter`() {
        val requestEventFilter = RequestEventFilter(
            requestId = "create-id",
            debug = Debug(mode = WorkModeDto.PROD),
            filter = EventFilterDto(
                frequency = FrequencyDto.DAILY,
                from = 21,
                pageSize = 20,
            )
        )
        val context = Context()

        context.requestFilter(requestEventFilter)
        assertThat(context.requestEventFilter).isEqualTo(
            FilterModel(
                from = 21,
                pageSize = 20,
                frequencyModel = FrequencyModel.DAILY
            )
        )
    }

    @Test
    fun `convert to response create event when error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
        )
        context.errors = mutableListOf(
            ErrorValueModel(
                code = "code",
                field = "field",
                message = "message",
            )
        )

        val responseEventCreateDto = context.toResponseEventCreate()

        assertThat(responseEventCreateDto).isEqualTo(
            ResponseEventCreate(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.ERROR,
                errors = listOf(
                    ErrorValueDto(
                        code = "code",
                        field = "field",
                        message = "message",
                    )
                )
            )
        )
    }

    @Test
    fun `convert to response create event without error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
        )
        context.responseEvent = EventModel(
            id = EventIdModel("event-id"),
            name = "День рождения жены",
            description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
            startSchedule = LocalDateTime.parse("2021-02-25T13:40:00", DateTimeFormatter.ISO_DATE_TIME),
            frequency = FrequencyModel.YEARLY,
            userId = UserId("user-id"),
            mobile = "+7123456789",
        )

        val responseEventCreateDto = context.toResponseEventCreate()

        assertThat(responseEventCreateDto).isEqualTo(
            ResponseEventCreate(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.SUCCESS,
                event = EventDto(
                    id = "event-id",
                    name = "День рождения жены",
                    description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
                    startSchedule = "2021-02-25 13:40",
                    userId = "user-id",
                    frequency = FrequencyDto.YEARLY,
                    mobile = "+7123456789",
                ),
                errors = listOf()
            )
        )
    }

    @Test
    fun `convert to response update event when error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
        )
        context.errors = mutableListOf(
            ErrorValueModel(
                code = "code",
                field = "field",
                message = "message",
            )
        )

        val responseEventUpdate = context.toResponseEventUpdate()

        assertThat(responseEventUpdate).isEqualTo(
            ResponseEventUpdate(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.ERROR,
                errors = listOf(
                    ErrorValueDto(
                        code = "code",
                        field = "field",
                        message = "message",
                    )
                )
            )
        )
    }

    @Test
    fun `convert to response update event without error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
        )
        context.responseEvent = EventModel(
            id = EventIdModel("event-id"),
            name = "День рождения жены",
            description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
            startSchedule = LocalDateTime.parse("2021-02-25T13:40:00", DateTimeFormatter.ISO_DATE_TIME),
            frequency = FrequencyModel.YEARLY,
            userId = UserId("user-id"),
            mobile = "+7123456789",
        )

        val responseEventUpdate = context.toResponseEventUpdate()

        assertThat(responseEventUpdate).isEqualTo(
            ResponseEventUpdate(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.SUCCESS,
                event = EventDto(
                    id = "event-id",
                    name = "День рождения жены",
                    description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
                    startSchedule = "2021-02-25 13:40",
                    userId = "user-id",
                    frequency = FrequencyDto.YEARLY,
                    mobile = "+7123456789",
                ),
                errors = listOf()
            )
        )
    }

    @Test
    fun `convert to response read event when error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
        )
        context.errors = mutableListOf(
            ErrorValueModel(
                code = "code",
                field = "field",
                message = "message",
            )
        )

        val responseEventRead = context.toResponseEventRead()

        assertThat(responseEventRead).isEqualTo(
            ResponseEventRead(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.ERROR,
                errors = listOf(
                    ErrorValueDto(
                        code = "code",
                        field = "field",
                        message = "message",
                    )
                )
            )
        )
    }

    @Test
    fun `convert to response read event without error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
        )
        context.responseEvent = EventModel(
            id = EventIdModel("event-id"),
            name = "День рождения жены",
            description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
            startSchedule = LocalDateTime.parse("2021-02-25T13:40:00", DateTimeFormatter.ISO_DATE_TIME),
            frequency = FrequencyModel.YEARLY,
            userId = UserId("user-id"),
            mobile = "+7123456789",
        )

        val responseEventRead = context.toResponseEventRead()

        assertThat(responseEventRead).isEqualTo(
            ResponseEventRead(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.SUCCESS,
                event = EventDto(
                    id = "event-id",
                    name = "День рождения жены",
                    description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
                    startSchedule = "2021-02-25 13:40",
                    userId = "user-id",
                    frequency = FrequencyDto.YEARLY,
                    mobile = "+7123456789",
                ),
                errors = listOf()
            )
        )
    }

    @Test
    fun `convert to response delete event when error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
        )
        context.errors = mutableListOf(
            ErrorValueModel(
                code = "code",
                field = "field",
                message = "message",
            )
        )

        val responseEventDelete = context.toResponseEventDelete()

        assertThat(responseEventDelete).isEqualTo(
            ResponseEventDelete(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.ERROR,
                errors = listOf(
                    ErrorValueDto(
                        code = "code",
                        field = "field",
                        message = "message",
                    )
                ),
                deleted = false,
            )
        )
    }

    @Test
    fun `convert to response delete event without error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
        )
        context.responseEvent = EventModel(
            id = EventIdModel("event-id"),
            name = "День рождения жены",
            description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
            startSchedule = LocalDateTime.parse("2021-02-25T13:40:00", DateTimeFormatter.ISO_DATE_TIME),
            frequency = FrequencyModel.YEARLY,
            userId = UserId("user-id"),
            mobile = "+7123456789",
        )

        val responseEventDelete = context.toResponseEventDelete()

        assertThat(responseEventDelete).isEqualTo(
            ResponseEventDelete(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.SUCCESS,
                event = EventDto(
                    id = "event-id",
                    name = "День рождения жены",
                    description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
                    startSchedule = "2021-02-25 13:40",
                    userId = "user-id",
                    frequency = FrequencyDto.YEARLY,
                    mobile = "+7123456789",
                ),
                errors = listOf(),
                deleted = true,
            )
        )
    }


    @Test
    fun `convert to response event filter when error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
        )
        context.errors = mutableListOf(
            ErrorValueModel(
                code = "code",
                field = "field",
                message = "message",
            )
        )

        val responseEventFilter = context.toResponseEventFilter()

        assertThat(responseEventFilter).isEqualTo(
            ResponseEventFilter(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.ERROR,
                events = listOf(),
                errors = listOf(
                    ErrorValueDto(
                        code = "code",
                        field = "field",
                        message = "message",
                    )
                )
            )
        )
    }

    @Test
    fun `convert to response event filter without error`() {
        val context = Context(
            onRequestId = "request-id",
            requestEventId = EventIdModel("event-id"),
            startTime = LocalDateTime.now().minusMinutes(1),
            eventsCount = 103,
        )
        context.responseEventFilter = listOf(
            EventModel(
                id = EventIdModel("event-id"),
                name = "День рождения жены",
                description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
                startSchedule = LocalDateTime.parse("2021-02-25T13:40:00", DateTimeFormatter.ISO_DATE_TIME),
                frequency = FrequencyModel.YEARLY,
                userId = UserId("user-id"),
                mobile = "+7123456789",
            )
        )

        val responseEventFilter = context.toResponseEventFilter()

        assertThat(responseEventFilter).isEqualTo(
            ResponseEventFilter(
                onRequestId = "request-id",
                responseId = "2b5edf5c-0388-4f55-9b05-675460e0462d",
                startTime = "2020-01-01T10:19:00",
                endTime = "2020-01-01T10:20:00",
                status = ResponseStatusDto.SUCCESS,
                events = listOf(
                    EventDto(
                        id = "event-id",
                        name = "День рождения жены",
                        description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
                        startSchedule = "2021-02-25 13:40",
                        userId = "user-id",
                        frequency = FrequencyDto.YEARLY,
                        mobile = "+7123456789",
                    )
                ),
                errors = listOf(),
                count = 103,
            )
        )
    }
}