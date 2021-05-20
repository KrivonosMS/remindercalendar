package ru.otus.otuskotlin.remindercalendar.ktor.controller

import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.assertj.core.api.Assertions.assertThat
import ru.otus.otuskotlin.remindercalendar.ktor.jsonConfig
import ru.otus.otuskotlin.remindercalendar.ktor.module
import ru.otus.otuskotlin.remindercalendar.transport.model.common.*
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*
import java.time.LocalDateTime
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.fail

internal class EventControllerTest {

    private val guid = "2b5edf5c-0388-4f55-9b05-675460e0462d"

    @BeforeTest
    fun mockInit() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.of(2020, 1, 1, 10, 20)
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString(guid)
    }

    @AfterTest
    fun destory() {
        unmockkStatic(LocalDateTime::class)
        unmockkStatic(UUID::class)
    }

    @Test
    fun `event read when success`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/event/read") {
                val body = RequestEventRead(
                    requestId = "1",
                    eventId = "event-id",
                    debug = Debug(stubCase = StubCase.SUCCESS),
                )
                val bodyString = jsonConfig.encodeToString(Message.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.contentType()).isEqualTo(ContentType.Application.Json.withCharset(Charsets.UTF_8))
                val jsonString = response.content ?: fail("empty content")
                val responseEventRead =
                    jsonConfig.decodeFromString(Message.serializer(), jsonString) as ResponseEventRead
                assertThat(responseEventRead)
                    .isInstanceOf(ResponseEventRead::class.java)
                    .isEqualToComparingFieldByField(
                        ResponseEventRead(
                            responseId = guid,
                            onRequestId = "1",
                            startTime = "2020-01-01T10:20:00",
                            endTime = "2020-01-01T10:20:00",
                            status = ResponseStatusDto.SUCCESS,
                            errors = listOf(),
                            event = EventDto(
                                id = "test-id",
                                name = "name",
                                description = "test-description",
                                startSchedule = "2020-02-25T07:22:00",
                                userId = "test-user-id",
                                frequency = FrequencyDto.DAILY,
                                mobile = "test-mobile",
                            )
                        )
                    )
            }
        }
    }

    @Test
    fun `event create when success`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/event/create") {
                val body = RequestEventCreate(
                    requestId = "1",
                    debug = Debug(stubCase = StubCase.SUCCESS),
                    event = EventCreateDto(
                        name = "name",
                        description = "description",
                        startSchedule = "2021-02-25T13:40:00",
                        userId = "userId",
                        frequency = FrequencyDto.YEARLY,
                        mobile = "+7123456789",
                    )
                )
                val bodyString = jsonConfig.encodeToString(Message.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.contentType()).isEqualTo(ContentType.Application.Json.withCharset(Charsets.UTF_8))
                val jsonString = response.content ?: fail("empty content")
                val responseEventCreate =
                    jsonConfig.decodeFromString(Message.serializer(), jsonString) as ResponseEventCreate
                assertThat(responseEventCreate)
                    .isInstanceOf(ResponseEventCreate::class.java)
                    .isEqualToComparingFieldByField(
                        ResponseEventCreate(
                            responseId = guid,
                            onRequestId = "1",
                            startTime = "2020-01-01T10:20:00",
                            endTime = "2020-01-01T10:20:00",
                            status = ResponseStatusDto.SUCCESS,
                            errors = listOf(),
                            event = EventDto(
                                id = "test-id",
                                name = "name",
                                description = "test-description",
                                startSchedule = "2020-02-25T07:22:00",
                                userId = "test-user-id",
                                frequency = FrequencyDto.DAILY,
                                mobile = "test-mobile",
                            )
                        )
                    )
            }
        }
    }

    @Test
    fun `event update when success`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/event/update") {
                val body = RequestEventUpdate(
                    requestId = "1",
                    debug = Debug(stubCase = StubCase.SUCCESS),
                    event = EventUpdateDto(
                        id = "id",
                        name = "name",
                        description = "description",
                        startSchedule = "2021-02-25T13:40:00",
                        userId = "userId",
                        frequency = FrequencyDto.YEARLY,
                        mobile = "+7123456789",
                    )
                )
                val bodyString = jsonConfig.encodeToString(Message.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.contentType()).isEqualTo(ContentType.Application.Json.withCharset(Charsets.UTF_8))
                val jsonString = response.content ?: fail("empty content")
                val responseEventUpdate =
                    jsonConfig.decodeFromString(Message.serializer(), jsonString) as ResponseEventUpdate
                assertThat(responseEventUpdate)
                    .isInstanceOf(ResponseEventUpdate::class.java)
                    .isEqualToComparingFieldByField(
                        ResponseEventUpdate(
                            responseId = guid,
                            onRequestId = "1",
                            startTime = "2020-01-01T10:20:00",
                            endTime = "2020-01-01T10:20:00",
                            status = ResponseStatusDto.SUCCESS,
                            errors = listOf(),
                            event = EventDto(
                                id = "test-id",
                                name = "name",
                                description = "test-description",
                                startSchedule = "2020-02-25T07:22:00",
                                userId = "test-user-id",
                                frequency = FrequencyDto.DAILY,
                                mobile = "test-mobile",
                            )
                        )
                    )
            }
        }
    }

    @Test
    fun `event delete when success`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/event/delete") {
                val body = RequestEventDelete(
                    requestId = "1",
                    debug = Debug(stubCase = StubCase.SUCCESS),
                    eventId = "event-id",
                )
                val bodyString = jsonConfig.encodeToString(Message.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.contentType()).isEqualTo(ContentType.Application.Json.withCharset(Charsets.UTF_8))
                val jsonString = response.content ?: fail("empty content")
                val responseEventDelete =
                    jsonConfig.decodeFromString(Message.serializer(), jsonString) as ResponseEventDelete
                assertThat(responseEventDelete)
                    .isInstanceOf(ResponseEventDelete::class.java)
                    .isEqualToComparingFieldByField(
                        ResponseEventDelete(
                            responseId = guid,
                            onRequestId = "1",
                            startTime = "2020-01-01T10:20:00",
                            endTime = "2020-01-01T10:20:00",
                            status = ResponseStatusDto.SUCCESS,
                            deleted = true,
                            errors = listOf(),
                            event = EventDto(
                                id = "test-id",
                                name = "name",
                                description = "test-description",
                                startSchedule = "2020-02-25T07:22:00",
                                userId = "test-user-id",
                                frequency = FrequencyDto.DAILY,
                                mobile = "test-mobile",
                            )
                        )
                    )
            }
        }
    }

    @Test
    fun `event filter when success`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/event/filter") {
                val body = RequestEventFilter(
                    requestId = "1",
                    debug = Debug(stubCase = StubCase.SUCCESS),
                    filter = EventFilterDto(
                        frequency = FrequencyDto.YEARLY,
                        from = 10,
                        pageSize = 1,
                    )
                )
                val bodyString = jsonConfig.encodeToString(Message.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.contentType()).isEqualTo(ContentType.Application.Json.withCharset(Charsets.UTF_8))
                val jsonString = response.content ?: fail("empty content")
                val responseEventFilter =
                    jsonConfig.decodeFromString(Message.serializer(), jsonString) as ResponseEventFilter
                assertThat(responseEventFilter)
                    .isInstanceOf(ResponseEventFilter::class.java)
                    .isEqualToComparingFieldByField(
                        ResponseEventFilter(
                            responseId = guid,
                            onRequestId = "1",
                            startTime = "2020-01-01T10:20:00",
                            endTime = "2020-01-01T10:20:00",
                            status = ResponseStatusDto.SUCCESS,
                            errors = listOf(),
                            events = listOf(
                                EventDto(
                                    id = "test-id",
                                    name = "name",
                                    description = "test-description",
                                    startSchedule = "2020-02-25T07:22:00",
                                    userId = "test-user-id",
                                    frequency = FrequencyDto.DAILY,
                                    mobile = "test-mobile",
                                ),
                            ),
                            count = 100,
                        )
                    )
            }
        }
    }
}