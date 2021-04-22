package ru.otus.otuskotlin.remindercalendar.repository.sql.events

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventIdModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventModel
import ru.otus.otuskotlin.remindercalendar.common.backend.repositories.EventRepository
import ru.otus.otuskotlin.remindercalendar.common.backend.repositories.RepositoryNotFoundException
import ru.otus.otuskotlin.remindercalendar.common.backend.repositories.RepositoryWrongIdException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class EventRepositoryInMemory @OptIn(ExperimentalTime::class) constructor(
    ttl: Duration,
    initObjects: Collection<EventModel> = emptyList()
): EventRepository {
    @OptIn(ExperimentalTime::class)
    private var cache: Cache<String, EventInMemoryDto> = object : Cache2kBuilder<String, EventInMemoryDto>() {}
        .expireAfterWrite(ttl.toLongMilliseconds(), TimeUnit.MILLISECONDS)
        .suppressExceptions(false)
        .build()
        .also { cache ->
            initObjects.forEach {
                cache.put(it.id.id, EventInMemoryDto.of(it))
            }
        }

    override suspend fun read(context: Context): EventModel {
        val id = context.requestEventId
        if (id == EventIdModel.NONE) throw RepositoryWrongIdException(id.id)
        val model = cache.get(id.id)?.toModel()?: throw RepositoryNotFoundException(id.id)
        context.responseEvent = model
        return model
    }

    override suspend fun create(context: Context): EventModel {
        val dto = EventInMemoryDto.of(context.requestEvent, UUID.randomUUID().toString())
        val model = save(dto).toModel()
        context.responseEvent = model
        return model
    }

    override suspend fun update(context: Context): EventModel {
        if (context.requestEvent.id == EventIdModel.NONE) throw RepositoryWrongIdException(context.requestEvent.id.id)
        val model = save(EventInMemoryDto.of(context.requestEvent)).toModel()
        context.responseEvent = model
        return model
    }

    override suspend fun delete(context: Context): EventModel {
        val id = context.requestEventId
        if (id == EventIdModel.NONE) throw RepositoryWrongIdException(id.id)
        val model = cache.peekAndRemove(id.id)?.toModel()?: throw RepositoryNotFoundException(id.id)
        context.responseEvent = model
        return model
    }

    override suspend fun filter(context: Context): Collection<EventModel> {
        val frequencyModel = context.requestEventFilter.frequencyModel
        val records = cache.asMap().filterValues { it.frequency == frequencyModel }.values
        if (records.count() <= context.requestEventFilter.from) {
            return listOf()
        }
        val list = records.toList()
            .subList(
                context.requestEventFilter.from,
                if (records.count() >= context.requestEventFilter.from + context.requestEventFilter.pageSize) {
                    context.requestEventFilter.from + context.requestEventFilter.pageSize
                } else {
                    records.count()
                }
            ).map { it.toModel() }
        context.responseEventFilter = list.toMutableList()
        context.eventsCount = records.count()
        return list
    }

    private suspend fun save(dto: EventInMemoryDto): EventInMemoryDto {
        cache.put(dto.id, dto)
        return cache.get(dto.id)
    }
}
