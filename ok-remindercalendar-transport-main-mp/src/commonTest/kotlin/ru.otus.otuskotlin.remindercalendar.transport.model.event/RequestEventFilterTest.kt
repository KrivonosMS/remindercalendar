package ru.otus.otuskotlin.remindercalendar.transport.model.event

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Debug
import ru.otus.otuskotlin.remindercalendar.transport.model.common.FrequencyDto
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.common.WorkModeDto
import kotlin.test.Test
import kotlin.test.assertEquals

class RequestEventFilterTest {
    @Test
    fun serializationEventsByFilterRequest() {
        val jsonRequest = Json {
            prettyPrint = true
            serializersModule = SerializersModule {
                polymorphic(Message::class) {
                    subclass(RequestEventFilter::class, RequestEventFilter.serializer())
                }

            }
            classDiscriminator = "type"
        }
        val dto: Message = RequestEventFilter(
            requestId = "request-id",
            onResponseId = "response-id",
            startTime = "2021-02-13T12:00:00",
            debug = Debug(mode = WorkModeDto.PROD),
            filter = EventFilterDto(
                frequency = FrequencyDto.DAILY,
                from = 0,
                pageSize = 20,
            )
        )
        val serializedString = jsonRequest.encodeToString(dto)
        assertEquals(
            """{
    "type": "RequestEventFilter",
    "requestId": "request-id",
    "onResponseId": "response-id",
    "startTime": "2021-02-13T12:00:00",
    "debug": {
        "mode": "PROD"
    },
    "filter": {
        "frequency": "DAILY",
        "from": 0,
        "pageSize": 20
    }
}""",
            serializedString
        )
        val deserializedDto = jsonRequest.decodeFromString(Message.serializer(), serializedString)
        assertEquals(dto, deserializedDto as RequestEventFilter)
    }
}