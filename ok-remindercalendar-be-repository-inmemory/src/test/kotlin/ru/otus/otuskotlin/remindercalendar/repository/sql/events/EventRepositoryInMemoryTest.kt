package ru.otus.otuskotlin.remindercalendar.repository.sql.events

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.FrequencyModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.UserId
import java.time.LocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

internal class EventRepositoryInMemoryTest {
    @OptIn(ExperimentalTime::class)
    @Test
    fun createAndGetTest() {
        val repository = EventRepositoryInMemory(
            ttl = 5.toDuration(DurationUnit.MINUTES)
        )

        val event = EventModel(
            description = "test-description",
            name = "name",
            startSchedule = LocalDateTime.of(2020, 2, 25, 7, 22),
            frequency = FrequencyModel.DAILY,
            userId = UserId("test-user-id"),
            mobile = "test-mobile",
        )

        val context = Context(
            requestEvent = event
        )

        runBlocking {
            val createdEvent = repository.create(context)
            Assertions.assertThat(createdEvent).isEqualToIgnoringGivenFields(event, "id")

            context.requestEventId = createdEvent.id
            val readEvent = repository.read(context)
            Assertions.assertThat(readEvent).isEqualToComparingFieldByField(createdEvent)
        }
    }
}