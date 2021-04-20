package ru.otus.otuskotlin.remindercalendar.ktor.controller

import io.ktor.routing.*
import ru.otus.otuskotlin.remindercalendar.ktor.helpers.handleRoute
import ru.otus.otuskotlin.remindercalendar.ktor.helpers.RestEndpoints
import ru.otus.otuskotlin.remindercalendar.ktor.services.EventService
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*

fun Routing.eventRouting(service: EventService) {
    post(RestEndpoints.eventCreate) {
        handleRoute<RequestEventCreate, ResponseEventCreate> { query ->
            service.create(this, query)
        }
    }
    post(RestEndpoints.eventRead) {
        handleRoute<RequestEventRead, ResponseEventRead> { query ->
            service.read(this, query)
        }
    }
    post(RestEndpoints.eventUpdate) {
        handleRoute<RequestEventUpdate, ResponseEventUpdate> { query ->
            service.update(this, query)
        }
    }
    post(RestEndpoints.eventDelete) {
        handleRoute<RequestEventDelete, ResponseEventDelete> { query ->
            service.delete(this, query)
        }
    }
    post(RestEndpoints.eventFilter) {
        handleRoute<RequestEventFilter, ResponseEventFilter> { query ->
            service.filter(this, query)
        }
    }
}