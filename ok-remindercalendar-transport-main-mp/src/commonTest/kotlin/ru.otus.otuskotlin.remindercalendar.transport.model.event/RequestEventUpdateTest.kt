package ru.otus.otuskotlin.remindercalendar.transport.model.event

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.otus.otuskotlin.remindercalendar.transport.model.common.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RequestEventUpdateTest {
    @Test
    fun serializationEventUpdateRequest() {
        val jsonRequest = Json {
            prettyPrint = true
            serializersModule = SerializersModule {
                polymorphic(Message::class) {
                    subclass(RequestEventUpdate::class, RequestEventUpdate.serializer())
                }

            }
            classDiscriminator = "type"
        }
        val dto: Message = RequestEventUpdate(
            requestId = "request-id",
            onResponseId = "response-id",
            startTime = "2021-02-13T12:00:00",
            debug = Debug(mode = WorkModeDto.PROD),
            event = EventUpdateDto(
                id = "id",
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
    "type": "RequestEventUpdate",
    "requestId": "request-id",
    "onResponseId": "response-id",
    "startTime": "2021-02-13T12:00:00",
    "debug": {
        "mode": "PROD"
    },
    "event": {
        "id": "id",
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
        assertEquals(dto, deserializedDto as RequestEventUpdate)
    }
}