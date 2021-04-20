package ru.otus.otuskotlin.remindercalendar.transport.model.event

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Debug
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.common.WorkModeDto
import kotlin.test.Test
import kotlin.test.assertEquals

class RequestEventDeleteTest {
    @Test
    fun serializationEventDeleteRequest() {
        val jsonRequest = Json {
            prettyPrint = true
            serializersModule = SerializersModule {
                polymorphic(Message::class) {
                    subclass(RequestEventDelete::class, RequestEventDelete.serializer())
                }

            }
            classDiscriminator = "type"
        }
        val dto: Message = RequestEventDelete(
            requestId = "request-id",
            onResponseId = "response-id",
            debug = Debug(mode = WorkModeDto.PROD),
            eventId = "event-id"
        )
        val serializedString = jsonRequest.encodeToString(dto)
        assertEquals(
            """{
    "type": "RequestEventDelete",
    "requestId": "request-id",
    "onResponseId": "response-id",
    "debug": {
        "mode": "PROD"
    },
    "eventId": "event-id"
}""",
            serializedString
        )
        val deserializedDto = jsonRequest.decodeFromString(Message.serializer(), serializedString)
        assertEquals(dto, deserializedDto as RequestEventDelete)
    }
}