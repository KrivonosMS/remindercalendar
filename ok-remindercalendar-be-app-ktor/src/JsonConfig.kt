package com.example

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*

val jsonConfig: Json by lazy {
    Json {
        prettyPrint = true
        serializersModule = SerializersModule {
            polymorphic(Message::class) {
                subclass(RequestEventRead::class)
                subclass(RequestEventCreate::class)
                subclass(RequestEventUpdate::class)
                subclass(RequestEventDelete::class)
                subclass(RequestEventFilter::class)
                subclass(ResponseEventRead::class)
                subclass(ResponseEventCreate::class)
                subclass(ResponseEventUpdate::class)
                subclass(ResponseEventDelete::class)
                subclass(ResponseEventFilter::class)
            }
        }
        classDiscriminator = "type"
    }
}
