package ru.otus.otuskotlin.remindercalendar.mapper

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.*
import ru.otus.otuskotlin.remindercalendar.transport.model.common.ErrorValueDto
import ru.otus.otuskotlin.remindercalendar.transport.model.common.FrequencyDto
import ru.otus.otuskotlin.remindercalendar.transport.model.common.FrequencyDto.*
import ru.otus.otuskotlin.remindercalendar.transport.model.common.ResponseStatusDto
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Context.request(request: RequestEventCreate) {
    requestEventId = EventIdModel.NONE
    requestEvent = request.event?.toInternalModel() ?: EventModel.NONE
}

fun EventCreateDto.toInternalModel() = EventModel(
    id = EventIdModel.NONE,
    name = name ?: "",
    description = description ?: "",
    startSchedule = startSchedule?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
        ?: LocalDateTime.MIN,
    frequency = frequency?.toInternalModel() ?: FrequencyModel.YEARLY,
    userId = userId?.let { UserId(it) } ?: UserId.NONE,
)

fun Context.request(request: RequestEventUpdate) {
    requestEventId = request.event?.let { it.id?.let { EventIdModel(it) } ?: EventIdModel.NONE } ?: EventIdModel.NONE
    requestEvent = request.event?.toInternalModel() ?: EventModel.NONE
}

fun EventUpdateDto.toInternalModel() = EventModel(
    id = id?.let { EventIdModel(it) } ?: EventIdModel.NONE,
    name = name ?: "",
    description = description ?: "",
    startSchedule = startSchedule?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
        ?: LocalDateTime.MIN,
    frequency = frequency?.toInternalModel() ?: FrequencyModel.YEARLY,
    userId = userId?.let { UserId(it) } ?: UserId.NONE,
)

fun Context.request(request: RequestEventRead) {
    requestEventId = request.eventId?.let { EventIdModel(it) } ?: EventIdModel.NONE
    requestEvent = EventModel.NONE
}

fun Context.request(request: RequestEventDelete) {
    requestEventId = request.eventId?.let { EventIdModel(it) } ?: EventIdModel.NONE
    requestEvent = EventModel.NONE
}

fun Context.requestFilter(requestFilter: RequestEventFilter) {
    requestEventFilter = requestFilter.filter?.let {
        FilterModel(
            from = it.from ?: 0,
            pageSize = it.pageSize ?: 0,
            frequencyModel = it.frequency?.let { it.toInternalModel() } ?: FrequencyModel.NONE

        )
    } ?: FilterModel.NONE
}

fun FrequencyDto.toInternalModel() =
    when (this) {
        HOURLY -> FrequencyModel.HOURLY
        DAILY -> FrequencyModel.DAILY
        WEEKLY -> FrequencyModel.WEEKLY
        YEARLY -> FrequencyModel.YEARLY
        else -> FrequencyModel.NONE
    }

fun Context.toResponseEventCreate() = ResponseEventCreate(
    responseId = requestEventId.id,
    onRequestId = requestEventId.id,
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    errors = errors.toErrorValueDtoList(),
    status = errors.status(),
    event = if (errors.status() == ResponseStatusDto.SUCCESS) responseEvent.toEventDto() else null,
)

fun List<ErrorValueModel>.status() = if (this.isEmpty()) {
    ResponseStatusDto.SUCCESS
} else {
    ResponseStatusDto.ERROR
}

fun List<ErrorValueModel>.toErrorValueDtoList() = this.map {
    ErrorValueDto(
        code = it.code,
        field = it.field,
        message = it.message,
    )
}

fun EventModel.toEventDto() = EventDto(
    id = this.id.id,
    name = this.name,
    description = this.description,
    startSchedule = this.startSchedule.format(DateTimeFormatter.ISO_DATE_TIME),
    userId = this.userId.id,
    frequency = this.frequency.toFrequencyDto(),
    mobile = this.mobile,
    permissions = null,
)

fun FrequencyModel.toFrequencyDto() =
    when (this) {
        FrequencyModel.HOURLY -> HOURLY
        FrequencyModel.DAILY -> DAILY
        FrequencyModel.WEEKLY -> WEEKLY
        FrequencyModel.YEARLY -> YEARLY
        else -> null
    }

fun Context.toResponseEventUpdate() = ResponseEventUpdate(
    responseId = requestEventId.id,
    onRequestId = requestEventId.id,
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    errors = errors.toErrorValueDtoList(),
    status = errors.status(),
    event = if (errors.status() == ResponseStatusDto.SUCCESS) responseEvent.toEventDto() else null,
)

fun Context.toResponseEventRead() = ResponseEventRead(
    responseId = requestEventId.id,
    onRequestId = requestEventId.id,
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    errors = errors.toErrorValueDtoList(),
    status = errors.status(),
    event = if (errors.status() == ResponseStatusDto.SUCCESS) responseEvent.toEventDto() else null,
)

fun Context.toResponseEventDelete() = ResponseEventDelete(
    responseId = requestEventId.id,
    onRequestId = requestEventId.id,
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    errors = errors.toErrorValueDtoList(),
    status = errors.status(),
    event = if (errors.status() == ResponseStatusDto.SUCCESS) responseEvent.toEventDto() else null,
    deleted = errors.status() == ResponseStatusDto.SUCCESS,
)

fun Context.toResponseEventFilter() = ResponseEventFilter(
    responseId = requestEventId.id,
    onRequestId = requestEventId.id,
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    errors = errors.toErrorValueDtoList(),
    status = errors.status(),
    events = if (errors.status() == ResponseStatusDto.SUCCESS) responseEventFilter.map { it.toEventDto() } else listOf(),
    count = if (errors.status() == ResponseStatusDto.SUCCESS) this.eventsCount else null,
)