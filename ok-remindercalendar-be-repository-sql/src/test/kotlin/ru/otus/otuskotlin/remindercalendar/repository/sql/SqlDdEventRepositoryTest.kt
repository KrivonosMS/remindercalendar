package ru.otus.otuskotlin.remindercalendar.repository.sql


import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")


internal class SqlDdEventRepositoryTest {

    companion object {
        private const val USER = "postgres"
        private const val PASS = "postgres"
        private const val DB = "remindercalendar"

        private val testEventId1 = EventIdModel("11111111-1111-1111-1111-111111111111")
        private val testEventId2 = EventIdModel("11111111-1111-1111-1111-111111111112")
        private val testEventId3 = EventIdModel("11111111-1111-1111-1111-111111111113")
        private val testEventId4 = EventIdModel("11111111-1111-1111-1111-111111111114")
        private val testEventId5 = EventIdModel("11111111-1111-1111-1111-111111111115")
        private val testEventId6 = EventIdModel("11111111-1111-1111-1111-111111111116")
        private val testEventId7 = EventIdModel("11111111-1111-1111-1111-111111111117")
        private val expectedEventId = EventIdModel("11111111-1111-1111-1111-111111111120")


        private val container by lazy {
            PostgresContainer().apply {
                withUsername(USER)
                withPassword(PASS)
                withDatabaseName(DB)
                withStartupTimeout(Duration.ofSeconds(300L))
                start()
            }
        }

        private val url by lazy { container.jdbcUrl }

        private val repository by lazy {
            SqlDbEventRepository(
                url = url,
                user = USER,
                password = PASS,
                printLogs = true,
                initObjects = listOf(
                    EventModel(
                        id = testEventId1,
                        description = "description-1",
                        name = "name-1",
                        startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                        frequency = FrequencyModel.DAILY,
                        userId = UserId("user-id-1"),
                        mobile = "mobile-1",
                    ),
                    EventModel(
                        id = testEventId2,
                        description = "description-2",
                        name = "name-2",
                        startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                        frequency = FrequencyModel.DAILY,
                        userId = UserId("user-id-2"),
                        mobile = "mobile-2",
                    ),
                    EventModel(
                        id = testEventId3,
                        description = "description-3",
                        name = "name-3",
                        startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                        frequency = FrequencyModel.DAILY,
                        userId = UserId("user-id-3"),
                        mobile = "mobile-3",
                    ),
                    EventModel(
                        id = testEventId4,
                        description = "description-4",
                        name = "name-4",
                        startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                        frequency = FrequencyModel.DAILY,
                        userId = UserId("user-id-4"),
                        mobile = "mobile-4",
                    ),
                    EventModel(
                        id = testEventId5,
                        description = "description-5",
                        name = "name-5",
                        startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                        frequency = FrequencyModel.DAILY,
                        userId = UserId("user-id-5"),
                        mobile = "mobile-5",
                    ),
                    EventModel(
                        id = testEventId6,
                        description = "description-6",
                        name = "name-6",
                        startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                        frequency = FrequencyModel.YEARLY,
                        userId = UserId("user-id-6"),
                        mobile = "mobile-6",
                    ),
                    EventModel(
                        id = testEventId7,
                        description = "description-7",
                        name = "name-7",
                        startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                        frequency = FrequencyModel.YEARLY,
                        userId = UserId("user-id-7"),
                        mobile = "mobile-7",
                    ),
                )
            )
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            container.close()
        }
    }

    @Test
    fun eventReadTest() {
        runBlocking {
            val context = Context(
                requestEventId = testEventId2
            )
            val model = repository.read(context)
            assertEquals(model, context.responseEvent)
            assertEquals("name-2", model.name)
        }
    }

    @Test
    fun eventListTest() {
        runBlocking {
            val context = Context(
                requestEventFilter = FilterModel(
                    from = 1,
                    pageSize = 2,
                    frequencyModel = FrequencyModel.DAILY,
                )
            )
            val response = repository.filter(context)
            assertEquals(response, context.responseEventFilter)
            assertEquals(5, context.eventsCount)
            assertEquals(2, response.size)
        }
    }

    @Test
    fun eventCreateTest() {
        repository
        mockkStatic(UUID::class) {
            every {
                UUID.randomUUID()
            } returns expectedEventId.asUUID()

            runBlocking {
                val event = EventModel(
                    description = "description-6",
                    name = "name-6",
                    startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                    frequency = FrequencyModel.DAILY,
                    userId = UserId("user-id-6"),
                    mobile = "mobile-6",
                )
                val context = Context(
                    requestEvent = event
                )
                val result = repository.create(context)
                assertEquals(result, context.responseEvent)
                assertEquals("name-6", result.name)
                assertEquals("description-6", result.description)
                val context2 = Context(requestEventId = result.id)
                repository.read(context2)
                assertEquals("name-6", context2.responseEvent.name)
                assertEquals("description-6", context2.responseEvent.description)
            }
        }
    }

    @Test
    fun eventUpdateTest() {
        runBlocking {
            val event = EventModel(
                id = testEventId6,
                description = "update-description-6",
                name = "update-6",
                startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
                frequency = FrequencyModel.WEEKLY,
                userId = UserId("user-id-6"),
                mobile = "mobile-6",
            )
            val context = Context(
                requestEvent = event
            )
            val result = repository.update(context)
            assertEquals(result, context.responseEvent)
            assertEquals("update-6", result.name)
            assertEquals(FrequencyModel.WEEKLY, result.frequency)
            val context2 = Context(requestEventId = testEventId6)
            repository.read(context2)
            assertEquals("update-6", context2.responseEvent.name)
            assertEquals(FrequencyModel.WEEKLY, context2.responseEvent.frequency)
        }
    }

    @Test
    fun demandDeleteTest() {
        runBlocking {
            val context = Context(
                requestEventId = testEventId7
            )
            val model = repository.delete(context)
            assertEquals(model, context.responseEvent)
            assertEquals("name-7", model.name)
        }
    }
}
