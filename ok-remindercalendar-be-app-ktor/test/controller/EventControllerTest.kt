package controller

import com.example.jsonConfig
import com.example.module
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import ru.otus.otuskotlin.remindercalendar.transport.model.common.ErrorValueDto
import ru.otus.otuskotlin.remindercalendar.transport.model.common.FrequencyDto
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.common.ResponseStatusDto
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*
import java.time.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.fail


internal class EventControllerTest {
    @BeforeTest
    fun mockInit() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.of(2020, 1, 1, 10, 20)
    }

    @Test
    fun `event read when success`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/event/read") {
                val body = RequestEventRead(
                    requestId = "1",
                    eventId = "event-id",
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
                            responseId = "123",
                            onRequestId = "1",
                            endTime = "2020-01-01T10:20",
                            status = ResponseStatusDto.SUCCESS,
                            event = EventDto(
                                id = "event-id",
                                name = "name",
                                description = "description",
                                startSchedule = "2021-02-25T13:40:00",
                                userId = "userId",
                                frequency = FrequencyDto.YEARLY,
                                mobile = "+7123456789",
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
                            responseId = "123",
                            onRequestId = "1",
                            endTime = "2020-01-01T10:20",
                            status = ResponseStatusDto.SUCCESS,
                            event = EventDto(
                                id = "event-id",
                                name = "name",
                                description = "description",
                                startSchedule = "2021-02-25T13:40:00",
                                userId = "userId",
                                frequency = FrequencyDto.YEARLY,
                                mobile = "+7123456789",
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
                            responseId = "123",
                            onRequestId = "1",
                            endTime = "2020-01-01T10:20",
                            status = ResponseStatusDto.SUCCESS,
                            event = EventDto(
                                id = "event-id",
                                name = "name",
                                description = "description",
                                startSchedule = "2021-02-25T13:40:00",
                                userId = "userId",
                                frequency = FrequencyDto.YEARLY,
                                mobile = "+7123456789",
                            )
                        )
                    )
            }
        }
    }

    @Test
    fun `event update when error`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/event/update") {
                val body = RequestEventUpdate(
                    requestId = "1",
                    event = EventUpdateDto(
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
                            responseId = "123",
                            onRequestId = "1",
                            endTime = "2020-01-01T10:20",
                            status = ResponseStatusDto.ERROR,
                            event = null,
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
        }
    }

    @Test
    fun `event delete when success`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/event/delete") {
                val body = RequestEventDelete(
                    requestId = "1",
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
                            responseId = "123",
                            onRequestId = "1",
                            endTime = "2020-01-01T10:20",
                            status = ResponseStatusDto.SUCCESS,
                            deleted = true,
                            event = EventDto(
                                id = "event-id",
                                name = "name",
                                description = "description",
                                startSchedule = "2021-02-25T13:40:00",
                                userId = "userId",
                                frequency = FrequencyDto.YEARLY,
                                mobile = "+7123456789",
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
                    filter = EventFilterDto(
                        frequency = FrequencyDto.YEARLY,
                        from = 10,
                        pageSize = 2,
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
                            responseId = "123",
                            onRequestId = "1",
                            endTime = "2020-01-01T10:20",
                            status = ResponseStatusDto.SUCCESS,
                            events = listOf(
                                EventDto(
                                    id = "event-id",
                                    name = "name",
                                    description = "description",
                                    startSchedule = "2021-02-25T13:40:00",
                                    userId = "userId",
                                    frequency = FrequencyDto.YEARLY,
                                    mobile = "+7123456789",
                                ),
                                EventDto(
                                    id = "event-id",
                                    name = "name",
                                    description = "description",
                                    startSchedule = "2021-02-25T13:40:00",
                                    userId = "userId",
                                    frequency = FrequencyDto.YEARLY,
                                    mobile = "+7123456789",
                                ),
                            ),
                            count = 100,
                        )
                    )
            }
        }
    }
}