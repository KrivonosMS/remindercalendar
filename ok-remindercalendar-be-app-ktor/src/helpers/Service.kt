package rru.otus.otuskotlin.remindercalendar.ktor.helpers

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.ktor.services.EventService
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*

suspend fun service(
    context: Context,
    query: Message?,
    eventService: EventService,
): Message? = when (query) {
    is RequestEventCreate -> eventService.create(context, query)
    is RequestEventRead -> eventService.read(context, query)
    is RequestEventUpdate -> eventService.update(context, query)
    is RequestEventDelete -> eventService.delete(context, query)
    is RequestEventFilter -> eventService.filter(context, query)

    else ->
        when {
            context.status == ContextStatus.FAILING -> eventService.filter(context, null)
            else -> null
        }
}
