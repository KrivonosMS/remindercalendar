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
import ru.otus.otuskotlin.marketplace.backend.app.ktor.configs.PostgresqlConfig
import ru.otus.otuskotlin.remindercalendar.business.logic.EventCrud
import ru.otus.otuskotlin.remindercalendar.common.backend.repositories.EventRepository
import ru.otus.otuskotlin.remindercalendar.ktor.controller.eventRouting
import ru.otus.otuskotlin.remindercalendar.ktor.controller.kafkaEndpoints
import ru.otus.otuskotlin.remindercalendar.ktor.services.EventService
import ru.otus.otuskotlin.remindercalendar.repository.sql.SqlDbEventRepository
import ru.otus.otuskotlin.remindercalendar.repository.sql.events.EventRepositoryInMemory
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@OptIn(ExperimentalTime::class)
@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(
    testing: Boolean = false,
    kafkaTestConsumer: Consumer<String, String>? = null,
    kafkaTestProducer: Producer<String, String>? = null,
    testEventRepository: EventRepository? = null,
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

    val dbConfig by lazy {
        PostgresqlConfig(environment)
    }


    val repositoryProdName by lazy {
        environment.config.property("remindercalendar.prod").getString().trim().toLowerCase()
    }

    val eventRepositoryProd: EventRepository = if (testing) {
        EventRepository.NONE
    } else {
        when (repositoryProdName) {
            "postgresql" -> SqlDbEventRepository(
                url = dbConfig.url,
                driver = dbConfig.driver,
                user = dbConfig.user,
                password = dbConfig.password,
                printLogs = dbConfig.printLogs,
            )
            else -> EventRepository.NONE
        }
    }

    val eventRepositoryTest = testEventRepository ?: EventRepositoryInMemory(ttl = 2.toDuration(DurationUnit.HOURS))

    val eventCrud = EventCrud(
        eventRepositoryProd = eventRepositoryProd,
        eventRepositoryTest = eventRepositoryTest,
    )

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

