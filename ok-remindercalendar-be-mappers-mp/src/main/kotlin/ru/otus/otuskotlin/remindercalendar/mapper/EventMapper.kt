package ru.otus.otuskotlin.remindercalendar.mapper

import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.*
import ru.otus.otuskotlin.remindercalendar.common.backend.model.StubCase.*
import ru.otus.otuskotlin.remindercalendar.transport.model.common.*
import ru.otus.otuskotlin.remindercalendar.transport.model.common.FrequencyDto.*
import ru.otus.otuskotlin.remindercalendar.transport.model.common.StubCase
import ru.otus.otuskotlin.remindercalendar.transport.model.event.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Context.request(request: RequestEventCreate) {
    onRequestId = request.requestId ?: ""
    requestEventId = request.requestId?.let { EventIdModel(it) } ?: EventIdModel.NONE
    requestEvent = request.event?.toInternalModel() ?: EventModel.NONE
    stubCase = when (request.debug?.stubCase) {
        StubCase.SUCCESS -> EVENT_CREATE_SUCCESS
        else -> NONE
    }
    workMode = request.debug?.mode.toModel()
}

internal fun WorkModeDto?.toModel() = when(this) {
    WorkModeDto.PROD -> WorkMode.PROD
    WorkModeDto.TEST -> WorkMode.TEST
    else -> WorkMode.DEFAULT
}

fun EventCreateDto.toInternalModel() = EventModel(
    id = EventIdModel.NONE,
    name = name ?: "",
    description = description ?: "",
    startSchedule = startSchedule?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) } ?: LocalDateTime.MIN,
    frequency = frequency?.toInternalModel() ?: FrequencyModel.YEARLY,
    userId = userId?.let { UserId(it) } ?: UserId.NONE,
    mobile = mobile ?: "",
)

fun Context.request(request: RequestEventUpdate) {
    onRequestId = request.requestId ?: ""
    requestEventId = request.requestId?.let { EventIdModel(it) } ?: EventIdModel.NONE
    requestEvent = request.event?.toInternalModel() ?: EventModel.NONE
    stubCase = when (request.debug?.stubCase) {
        StubCase.SUCCESS -> EVENT_UPDATE_SUCCESS
        else -> NONE
    }
    workMode = request.debug?.mode.toModel()
}

fun EventUpdateDto.toInternalModel() = EventModel(
    id = id?.let { EventIdModel(it) } ?: EventIdModel.NONE,
    name = name ?: "",
    description = description ?: "",
    startSchedule = startSchedule?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
        ?: LocalDateTime.MIN,
    frequency = frequency?.toInternalModel() ?: FrequencyModel.YEARLY,
    userId = userId?.let { UserId(it) } ?: UserId.NONE,
    mobile = mobile ?: "",
)

fun Context.request(request: RequestEventRead) {
    onRequestId = request.requestId ?: ""
    requestEventId = request.eventId?.let { EventIdModel(it) } ?: EventIdModel.NONE
    requestEvent = EventModel.NONE
    stubCase = when (request.debug?.stubCase) {
        StubCase.SUCCESS -> EVENT_READ_SUCCESS
        else -> NONE
    }
    workMode = request.debug?.mode.toModel()
}

fun Context.request(request: RequestEventDelete) {
    onRequestId = request.requestId ?: ""
    requestEventId = request.eventId?.let { EventIdModel(it) } ?: EventIdModel.NONE
    requestEvent = EventModel.NONE
    stubCase = when (request.debug?.stubCase) {
        StubCase.SUCCESS -> EVENT_DELETE_SUCCESS
        else -> NONE
    }
    workMode = request.debug?.mode.toModel()
}

fun Context.requestFilter(requestFilter: RequestEventFilter) {
    onRequestId = requestFilter.requestId ?: ""
    requestEventId = requestFilter.requestId?.let { EventIdModel(it) } ?: EventIdModel.NONE
    requestEventFilter = requestFilter.filter?.let {
        FilterModel(
            from = it.from ?: 0,
            pageSize = it.pageSize ?: 0,
            frequencyModel = it.frequency?.let { it.toInternalModel() } ?: FrequencyModel.NONE

        )
    } ?: FilterModel.NONE
    stubCase = when (requestFilter.debug?.stubCase) {
        StubCase.SUCCESS -> EVENT_FILTER_SUCCESS
        else -> NONE
    }
    workMode = requestFilter.debug?.mode.toModel()
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
    responseId = UUID.randomUUID().toString(),
    onRequestId = onRequestId,
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    startTime = this.startTime.format(DateTimeFormatter.ISO_DATE_TIME),
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
    startSchedule = this.startSchedule.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
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
    responseId = UUID.randomUUID().toString(),
    onRequestId = onRequestId,
    startTime = this.startTime.format(DateTimeFormatter.ISO_DATE_TIME),
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    errors = errors.toErrorValueDtoList(),
    status = errors.status(),
    event = if (errors.status() == ResponseStatusDto.SUCCESS) responseEvent.toEventDto() else null,
)

fun Context.toResponseEventRead() = ResponseEventRead(
    responseId = UUID.randomUUID().toString(),
    onRequestId = onRequestId,
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    startTime = this.startTime.format(DateTimeFormatter.ISO_DATE_TIME),
    errors = errors.toErrorValueDtoList(),
    status = errors.status(),
    event = if (errors.status() == ResponseStatusDto.SUCCESS) responseEvent.toEventDto() else null,
)

fun Context.toResponseEventDelete() = ResponseEventDelete(
    responseId = UUID.randomUUID().toString(),
    onRequestId = onRequestId,
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    startTime = this.startTime.format(DateTimeFormatter.ISO_DATE_TIME),
    errors = errors.toErrorValueDtoList(),
    status = errors.status(),
    event = if (errors.status() == ResponseStatusDto.SUCCESS) responseEvent.toEventDto() else null,
    deleted = errors.status() == ResponseStatusDto.SUCCESS,
)

fun Context.toResponseEventFilter() = ResponseEventFilter(
    responseId = UUID.randomUUID().toString(),
    onRequestId = onRequestId,
    endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    startTime = this.startTime.format(DateTimeFormatter.ISO_DATE_TIME),
    errors = errors.toErrorValueDtoList(),
    status = errors.status(),
    events = if (errors.status() == ResponseStatusDto.SUCCESS) responseEventFilter.map { it.toEventDto() } else listOf(),
    count = if (errors.status() == ResponseStatusDto.SUCCESS) this.eventsCount else null,
)