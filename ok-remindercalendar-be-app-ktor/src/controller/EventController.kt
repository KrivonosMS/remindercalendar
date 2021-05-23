package ru.otus.otuskotlin.remindercalendar.ktor.controller

import io.ktor.auth.*
import io.ktor.routing.*
import ru.otus.otuskotlin.remindercalendar.ktor.helpers.RestEndpoints
import ru.otus.otuskotlin.remindercalendar.ktor.helpers.handleRoute
import ru.otus.otuskotlin.remindercalendar.ktor.services.EventService
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*

fun Routing.eventRouting(service: EventService, authOff: Boolean = false) {
    authenticate("auth-jwt", optional = authOff) {
        post(RestEndpoints.eventCreate) {
            handleRoute<RequestEventCreate, ResponseEventCreate> { query ->
                useAuth = ! authOff
                service.create(this, query)
            }
        }
        post(RestEndpoints.eventRead) {
            handleRoute<RequestEventRead, ResponseEventRead> { query ->
                useAuth = ! authOff
                service.read(this, query)
            }
        }
        post(RestEndpoints.eventUpdate) {
            handleRoute<RequestEventUpdate, ResponseEventUpdate> { query ->
                useAuth = ! authOff
                service.update(this, query)
            }
        }
        post(RestEndpoints.eventDelete) {
            handleRoute<RequestEventDelete, ResponseEventDelete> { query ->
                useAuth = ! authOff
                service.delete(this, query)
            }
        }
        post(RestEndpoints.eventFilter) {
            handleRoute<RequestEventFilter, ResponseEventFilter> { query ->
                useAuth = ! authOff
                service.filter(this, query)
            }
        }
    }
}