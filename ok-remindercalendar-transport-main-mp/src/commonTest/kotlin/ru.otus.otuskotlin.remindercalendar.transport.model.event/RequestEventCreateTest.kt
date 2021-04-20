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

class RequestEventCreateTest {
    @Test
    fun serializationEventCreateRequest() {
        val jsonRequest = Json {
            prettyPrint = true
            serializersModule = SerializersModule {
                polymorphic(Message::class) {
                    subclass(RequestEventCreate::class, RequestEventCreate.serializer())
                }

            }
            classDiscriminator = "type"
        }
        val dto: Message = RequestEventCreate(
            requestId = "request-id",
            onResponseId = "response-id",
            debug = Debug(mode = WorkModeDto.PROD),
            event = EventCreateDto(
                name = "День рождения жены",
                description = "Этот день самый главный праздник в году. Про него никак нельзя забыть",
                startSchedule = "2021-02-25T13:40:00",
                userId = "user-id",
                frequency = FrequencyDto.YEARLY,
                mobile = "+7123456789"

            )
        )
        val serializedString = jsonRequest.encodeToString(dto)
        assertEquals(
            """{
    "type": "RequestEventCreate",
    "requestId": "request-id",
    "onResponseId": "response-id",
    "debug": {
        "mode": "PROD"
    },
    "event": {
        "name": "День рождения жены",
        "description": "Этот день самый главный праздник в году. Про него никак нельзя забыть",
        "startSchedule": "2021-02-25T13:40:00",
        "userId": "user-id",
        "frequency": "YEARLY",
        "mobile": "+7123456789"
    }
}""",
            serializedString
        )
        val deserializedDto = jsonRequest.decodeFromString(Message.serializer(), serializedString)
        assertEquals(dto, deserializedDto as RequestEventCreate)
    }
}