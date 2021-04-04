package com.example

import com.example.controller.EventController
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.features.*
import io.ktor.serialization.*
import ru.otus.otuskotlin.remindercalendar.business.logic.EventCrud

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
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
    val eventController = EventController(eventCrud)

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        route("/event") {
            post("/read") {
                eventController.read(this)
            }
            post("/create") {
                eventController.create(this)
            }
            post("/update") {
                eventController.update(this)
            }
            post("/delete") {
                eventController.delete(this)
            }
            post("/filter") {
                eventController.filer(this)
            }

        }
    }
}

