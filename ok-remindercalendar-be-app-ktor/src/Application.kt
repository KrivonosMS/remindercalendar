package ru.otus.otuskotlin.remindercalendar.ktor

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import ru.otus.otuskotlin.remindercalendar.business.logic.EventCrud
import ru.otus.otuskotlin.remindercalendar.ktor.controller.eventRouting
import ru.otus.otuskotlin.remindercalendar.ktor.controller.kafkaEndpoints
import ru.otus.otuskotlin.remindercalendar.ktor.services.EventService

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(
    testing: Boolean = false,
    kafkaTestConsumer: Consumer<String, String>? = null,
    kafkaTestProducer: Producer<String, String>? = null,
) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ContentNegotiation) {
        json(
            contentType = ContentType.Application.Json,
            json = jsonConfig,
        )
    }

    val eventCrud = EventCrud()
    val eventService = EventService(eventCrud)

    val topicIn = environment.config.propertyOrNull("remindercalendar.kafka.topicIn")?.getString()
    if (topicIn != null) {
        kafkaEndpoints(
            kafkaConsumer = kafkaTestConsumer,
            kafkaProducer = kafkaTestProducer,
            eventService = eventService,
        )
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        eventRouting(eventService)
    }

}

