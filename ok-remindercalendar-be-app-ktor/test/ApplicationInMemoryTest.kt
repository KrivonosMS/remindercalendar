import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.assertj.core.api.Assertions.assertThat
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventIdModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.FrequencyModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.UserId
import ru.otus.otuskotlin.remindercalendar.ktor.helpers.RestEndpoints
import ru.otus.otuskotlin.remindercalendar.ktor.jsonConfig
import ru.otus.otuskotlin.remindercalendar.ktor.module
import ru.otus.otuskotlin.remindercalendar.mapper.toFrequencyDto
import ru.otus.otuskotlin.remindercalendar.repository.sql.events.EventRepositoryInMemory
import ru.otus.otuskotlin.remindercalendar.transport.model.common.*
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*
import java.time.LocalDateTime
import java.util.*
import kotlin.test.*
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@OptIn(ExperimentalTime::class)
internal class ApplicationInMemoryTest {

    @BeforeTest
    fun mockInit() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString("2b5edf5c-0388-4f55-9b05-675460e0462d")
    }

    @AfterTest
    fun destory() {
        unmockkStatic(UUID::class)
    }

    companion object {
        val event1 = EventModel(
            id = EventIdModel("test-id-1"),
            description = "test-description-1",
            name = "name",
            startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
            frequency = FrequencyModel.DAILY,
            userId = UserId("test-user-id-1"),
            mobile = "test-mobile-1",
        )
        val event2 = EventModel(
            id = EventIdModel("test-id-2"),
            description = "test-description-2",
            name = "name",
            startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
            frequency = FrequencyModel.YEARLY,
            userId = UserId("test-user-id-2"),
            mobile = "test-mobile-1",
        )
        val event3 = EventModel(
            id = EventIdModel("test-id-3"),
            description = "test-description-3",
            name = "name",
            startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
            frequency = FrequencyModel.DAILY,
            userId = UserId("test-user-id-3"),
            mobile = "test-mobile-3",
        )
        val event4 = EventModel(
            id = EventIdModel("test-id-4"),
            description = "test-description-4",
            name = "name",
            startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
            frequency = FrequencyModel.DAILY,
            userId = UserId("test-user-id-4"),
            mobile = "test-mobile-4",
        )

        val eventRepositoryInMemory by lazy {
            EventRepositoryInMemory(
                ttl = 15.toDuration(DurationUnit.MINUTES),
                initObjects = listOf(event1, event2, event3, event4)
            )
        }
    }

    @Test
    fun testRead() {
        withTestApplication({
            module(
                testEventRepository = eventRepositoryInMemory,
                testing = true,
            )
        }) {
            handleRequest(HttpMethod.Post, RestEndpoints.eventRead) {
                val body = RequestEventRead(
                    requestId = "1",
                    eventId = "test-id-1",
                    debug = Debug(mode = WorkModeDto.TEST),
                )
                val bodyString = jsonConfig.encodeToString(Message.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val responseEventRead =
                    (jsonConfig.decodeFromString(Message.serializer(), jsonString) as? ResponseEventRead)
                        ?: fail("Incorrect response format")

                assertThat(responseEventRead.status).isEqualTo(ResponseStatusDto.SUCCESS)
                assertThat(responseEventRead.onRequestId).isEqualTo("1")
                assertThat(responseEventRead.event?.id).isEqualTo(event1.id.id)
                assertThat(responseEventRead.event?.name).isEqualTo(event1.name)
                assertThat(responseEventRead.event?.startSchedule).isEqualTo("2020-02-25 07:22")
                assertThat(responseEventRead.event?.description).isEqualTo(event1.description)
                assertThat(responseEventRead.event?.mobile).isEqualTo(event1.mobile)
                assertThat(responseEventRead.event?.frequency).isEqualTo(event1.frequency.toFrequencyDto())
            }
        }
    }

    @Test
    fun testCreate() {
        withTestApplication({
            module(
                testEventRepository = eventRepositoryInMemory,
                testing = true,
            )
        }) {
            handleRequest(HttpMethod.Post, RestEndpoints.eventCreate) {
                val body = RequestEventCreate(
                    requestId = "1",
                    debug = Debug(mode = WorkModeDto.TEST),
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
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val responseEventCreate =
                    (jsonConfig.decodeFromString(Message.serializer(), jsonString) as? ResponseEventCreate)
                        ?: fail("Incorrect response format")

                assertThat(responseEventCreate.status).isEqualTo(ResponseStatusDto.SUCCESS)
                assertThat(responseEventCreate.onRequestId).isEqualTo("1")
                assertThat(responseEventCreate.event?.id).isEqualTo("2b5edf5c-0388-4f55-9b05-675460e0462d")
                assertThat(responseEventCreate.event?.name).isEqualTo("name")
                assertThat(responseEventCreate.event?.startSchedule).isEqualTo("2021-02-25 13:40")
                assertThat(responseEventCreate.event?.description).isEqualTo("description")
                assertThat(responseEventCreate.event?.mobile).isEqualTo("+7123456789")
                assertThat(responseEventCreate.event?.frequency).isEqualTo(FrequencyDto.YEARLY)
            }
        }
    }

    @Test
    fun testUpdate() {
        withTestApplication({
            module(
                testEventRepository = eventRepositoryInMemory,
                testing = true,
            )
        }) {
            handleRequest(HttpMethod.Post, RestEndpoints.eventUpdate) {
                val body = RequestEventUpdate(
                    requestId = "1",
                    debug = Debug(mode = WorkModeDto.TEST),
                    event = EventUpdateDto(
                        id = "test-id-2",
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
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val responseEventUpdate =
                    (jsonConfig.decodeFromString(Message.serializer(), jsonString) as? ResponseEventUpdate)
                        ?: fail("Incorrect response format")

                assertThat(responseEventUpdate.status).isEqualTo(ResponseStatusDto.SUCCESS)
                assertThat(responseEventUpdate.onRequestId).isEqualTo("1")
                assertThat(responseEventUpdate.event?.id).isEqualTo(event2.id.id)
                assertThat(responseEventUpdate.event?.name).isEqualTo("name")
                assertThat(responseEventUpdate.event?.startSchedule).isEqualTo("2021-02-25 13:40")
                assertThat(responseEventUpdate.event?.description).isEqualTo("description")
                assertThat(responseEventUpdate.event?.mobile).isEqualTo("+7123456789")
                assertThat(responseEventUpdate.event?.frequency).isEqualTo(FrequencyDto.YEARLY)
            }
        }
    }

    @Test
    fun testDelete() {
        withTestApplication({
            module(
                testEventRepository = eventRepositoryInMemory,
                testing = true,
            )
        }) {
            handleRequest(HttpMethod.Post, RestEndpoints.eventDelete) {
                val body = RequestEventDelete(
                    requestId = "1",
                    debug = Debug(mode = WorkModeDto.TEST),
                    eventId = "test-id-3",
                )
                val bodyString = jsonConfig.encodeToString(Message.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val responseEventDelete =
                    (jsonConfig.decodeFromString(Message.serializer(), jsonString) as? ResponseEventDelete)
                        ?: fail("Incorrect response format")

                assertThat(responseEventDelete.status).isEqualTo(ResponseStatusDto.SUCCESS)
                assertThat(responseEventDelete.onRequestId).isEqualTo("1")
                assertThat(responseEventDelete.event?.id).isEqualTo(event3.id.id)
                assertThat(responseEventDelete.event?.name).isEqualTo(event3.name)
                assertThat(responseEventDelete.event?.startSchedule).isEqualTo("2020-02-25 07:22")
                assertThat(responseEventDelete.event?.description).isEqualTo(event3.description)
                assertThat(responseEventDelete.event?.mobile).isEqualTo(event3.mobile)
                assertThat(responseEventDelete.event?.frequency).isEqualTo(event3.frequency.toFrequencyDto())
            }
        }
    }

    @Test
    fun testFilter() {
        withTestApplication({
            module(
                testEventRepository = eventRepositoryInMemory,
                testing = true,
            )
        }) {
            handleRequest(HttpMethod.Post, RestEndpoints.eventFilter) {
                val body = RequestEventFilter(
                    requestId = "1",
                    debug = Debug(mode = WorkModeDto.TEST),
                    filter = EventFilterDto(
                        frequency = FrequencyDto.DAILY,
                        from = 0,
                        pageSize = 10,
                    )
                )
                val bodyString = jsonConfig.encodeToString(Message.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val responseEventFilter =
                    (jsonConfig.decodeFromString(Message.serializer(), jsonString) as? ResponseEventFilter)
                        ?: fail("Incorrect response format")

                assertThat(responseEventFilter.status).isEqualTo(ResponseStatusDto.SUCCESS)
                assertThat(responseEventFilter.onRequestId).isEqualTo("1")
                assertThat(responseEventFilter.count).isEqualTo(2)
                assertThat(responseEventFilter.events)
                    .isNotNull
                    .hasSize(2)
            }
        }
    }
}
