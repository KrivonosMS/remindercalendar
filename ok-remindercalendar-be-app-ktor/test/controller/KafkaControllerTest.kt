package ru.otus.otuskotlin.remindercalendar.ktor.controller

import io.ktor.config.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import ru.datana.smart.common.ktor.kafka.TestConsumer
import ru.datana.smart.common.ktor.kafka.TestProducer
import ru.otus.otuskotlin.remindercalendar.ktor.jsonConfig
import ru.otus.otuskotlin.remindercalendar.ktor.module
import ru.otus.otuskotlin.remindercalendar.transport.model.common.*
import ru.otus.otuskotlin.remindercalendar.transport.model.event.EventCreateDto
import ru.otus.otuskotlin.remindercalendar.transport.model.event.RequestEventCreate
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

internal class KafkaControllerTest {

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

    @OptIn(KtorExperimentalAPI::class)
    @Test
    fun test() {
        val consumer: TestConsumer<String, String> = TestConsumer(duration = Duration.ofMillis(20))
        val producer: TestProducer<String, String> = TestProducer()
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("remindercalendar.kafka.topicIn", TOPIC_IN)
                put("remindercalendar.kafka.topicOut", TOPIC_OUT)
            }
            module(
                kafkaTestConsumer = consumer,
                kafkaTestProducer = producer,
                testing = true,
            )
        }) {
            runBlocking {
                delay(60)
                val jsonIn = jsonConfig.encodeToString(
                    Message.serializer(),
                    RequestEventCreate(
                        requestId = "create-id",
                        debug = Debug(stubCase = StubCase.SUCCESS),
                        event = EventCreateDto(
                            name = "День рождения жены",
                            description = "Этот день самый главный в году. Про него никак нельзя забыть",
                            startSchedule = "2020-02-25T07:22:00",
                            userId = "user-id",
                            frequency = FrequencyDto.YEARLY,
                            mobile = "+7123456789",
                        )
                    )
                )
                consumer.send(TOPIC_IN, "xx1", jsonIn)

                delay(1000L)

                val responseObjs = producer.getSent()
                assertTrue() {
                    val feedBack = responseObjs.first().value()
                    feedBack.contains(
                        """{
    "type": "ResponseEventCreate",
    "responseId": "$guid",
    "onRequestId": "create-id",
    "startTime": "2020-01-01T10:20:00",
    "endTime": "2020-01-01T10:20:00",
    "errors": [
    ],
    "status": "SUCCESS",
    "event": {
        "id": "test-id",
        "name": "name",
        "description": "test-description",
        "startSchedule": "2020-02-25T07:22:00",
        "userId": "test-user-id",
        "frequency": "DAILY",
        "mobile": "test-mobile"
    }
}"""
                    )
                }
            }
        }
    }

    companion object {
        const val TOPIC_IN = "some-topic-in"
        const val TOPIC_OUT = "some-topic-out"
    }
}

