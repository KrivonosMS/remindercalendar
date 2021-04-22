package ru.otus.otuskotlin.remindercalendar.repository.sql

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventIdModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.FrequencyModel
import ru.otus.otuskotlin.remindercalendar.common.backend.repositories.EventRepository
import ru.otus.otuskotlin.remindercalendar.common.backend.repositories.RepositoryWrongIdException
import ru.otus.otuskotlin.remindercalendar.repository.sql.schema.EventDto
import ru.otus.otuskotlin.remindercalendar.repository.sql.schema.EventsTable
import java.sql.Connection

class SqlDbEventRepository(
    url: String = "jdbc:postgresql://localhost:5432/remindercalendar",
    driver: String = "org.postgresql.Driver",
    user: String = "postgres",
    password: String = "postgres",
    private val printLogs: Boolean = false,
    initObjects: Collection<EventModel> = emptyList()
) : EventRepository {

    private val db by lazy {
        val _db = Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )
        transaction { SchemaUtils.create(EventsTable) }
        _db
    }

    init {
        runBlocking {
            initObjects.forEach {
                createWithId(Context(requestEvent = it), true)
            }
        }
    }

    override suspend fun filter(context: Context): Collection<EventModel> {
        val filter = context.requestEventFilter
        return transaction(
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE,
            repetitionAttempts = 3,
            db = db
        ) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val found =
                if (filter.frequencyModel != FrequencyModel.NONE) EventDto.find {
                    (EventsTable.frequency eq filter.frequencyModel.toString())
                }
                else EventDto.all()
            val result = found.limit(filter.pageSize.takeIf { it > 0 } ?: 20, filter.from.toLong().takeIf { it > 0 } ?: 0)

            context.responseEventFilter = result.map { it.toModel() }.toMutableList()
            context.eventsCount = found.count().toInt()
            context.responseEventFilter
        }
    }

    override suspend fun create(context: Context): EventModel = createWithId(context)

    private suspend fun createWithId(context: Context, setId: Boolean = false): EventModel {
        val model = context.requestEvent
        return transaction(db) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val eventNew = EventDto.new(if (setId) model.id.asUUID() else null) {
                of(model)
            }
            val eventNewId = eventNew.id
            context.responseEvent = EventDto[eventNewId].toModel()
            context.responseEvent
        }
    }

    override suspend fun read(context: Context): EventModel {
        val id = context.requestEventId
        if (id == EventIdModel.NONE) throw RepositoryWrongIdException(id.id)
        return transaction {
            if (printLogs) addLogger(StdOutSqlLogger)
            context.responseEvent = EventDto[id.asUUID()].toModel()
            context.responseEvent
        }
    }

    override suspend fun update(context: Context): EventModel {
        if (context.requestEvent.id == EventIdModel.NONE) throw RepositoryWrongIdException(context.requestEvent.id.id)
        val model = context.requestEvent
        val eventId = model.id.asUUID()
        return transaction(db) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val demandToUpdate = EventDto[eventId]
            demandToUpdate
                .apply { of(model) }
                .toModel()
            context.responseEvent = EventDto[eventId].toModel()
            context.responseEvent
        }
    }

    override suspend fun delete(context: Context): EventModel {
        val eventId = context.requestEventId
        if (eventId == EventIdModel.NONE) throw RepositoryWrongIdException(eventId.id)
        return transaction(db) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val old = EventDto[eventId.asUUID()]
            old.delete()
            context.responseEvent = old.toModel()
            context.responseEvent
        }
    }
}
