package ru.otus.otuskotlin.remindercalendar.ktor.controller

import io.ktor.application.*
import io.ktor.routing.*
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import rru.otus.otuskotlin.remindercalendar.ktor.helpers.service
import ru.datana.smart.common.ktor.kafka.KtorKafkaConsumer
import ru.datana.smart.common.ktor.kafka.kafka
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.ktor.helpers.toModel
import ru.otus.otuskotlin.remindercalendar.ktor.jsonConfig
import ru.otus.otuskotlin.remindercalendar.ktor.services.EventService
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Response
import java.time.LocalDateTime

fun Application.kafkaEndpoints(
    kafkaConsumer: Consumer<String, String>? =  null,
    kafkaProducer: Producer<String, String>? = null,
    eventService: EventService,
) {
    val topicIn by lazy { environment.config.property("remindercalendar.kafka.topicIn").getString() }
    val topicOut by lazy { environment.config.property("remindercalendar.kafka.topicOut").getString() }

    install(KtorKafkaConsumer)

    routing {
        kafkaController(
            topicIn = topicIn,
            topicOut = topicOut,
            kafkaConsumer = kafkaConsumer,
            kafkaProducer = kafkaProducer,
            eventService = eventService,
        )
    }
}

fun Routing.kafkaController(
    topicIn: String,
    topicOut: String,
    kafkaConsumer: Consumer<String, String>?,
    kafkaProducer: Producer<String, String>?,
    eventService: EventService,
) {
    kafka<String, String> {
        keyDeserializer = StringDeserializer::class.java
        valDeserializer = StringDeserializer::class.java
        consumer = kafkaConsumer

        topic(topicIn) {
            for (item in items.items) {
                val ctx = Context(
                    startTime = LocalDateTime.now()
                )
                try {
                    ctx.status = ContextStatus.RUNNING
                    val query = jsonConfig.decodeFromString(Message.serializer(), item.value)
                    service(
                        context = ctx,
                        query = query,
                        eventService = eventService,
                    )?.also {
                        kafkaProducer?.send(
                            ProducerRecord(
                                topicOut,
                                (it as Response).responseId,
                                jsonConfig.encodeToString(Message.serializer(), it)
                            )
                        )
                    }
                } catch (e: Throwable) {
                    ctx.status = ContextStatus.FAILING
                    ctx.errors.add(e.toModel())
                    service(
                        context = ctx,
                        query = null,
                        eventService = eventService,
                    )
                }
            }
        }
    }
}
