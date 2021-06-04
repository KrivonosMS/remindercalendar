package ru.otus.otuskotlin.remindercalendar.logging.pipelines

import ru.otus.otuskotlin.remindercalendar.logging.helpers.validation
import ru.otus.otuskotlin.remindercalendar.logging.operations.CompletePipeline
import ru.otus.otuskotlin.remindercalendar.logging.operations.InitializePipeline
import ru.otus.otuskotlin.remindercalendar.logging.operations.QuerySetWorkMode
import ru.otus.otuskotlin.remindercalendar.logging.operations.stubs.EventReadStub
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Permission
import ru.otus.otuskotlin.remindercalendar.common.backend.model.ErrorValueModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.EventModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.PrincipalModel
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorStringFieldNotEmpty
import ru.otus.otuskotlin.remindercalendar.logging.logger
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline

private val logger = logger(EventRead::class.java)

object EventRead : Operation<Context> by pipeline({
    execute(InitializePipeline)

    execute(QuerySetWorkMode)

    execute(EventReadStub)

    operation {
        startIf { status == ContextStatus.RUNNING && useAuth}
        execute {
            if (principal == PrincipalModel.NONE) {
                errors.add(
                    ErrorValueModel(
                        code = "unauthorized",
                        group = ErrorValueModel.Group.AUTH,
                        level = ErrorValueModel.Level.ERROR,
                        message = "User is unauthorized",
                    )
                )
                status = ContextStatus.ERROR
            }
        }
    }

    validation {
        validate<String?> {
            validator(ValidatorStringFieldNotEmpty("You must provide schedule event id"))
            on { requestEventId.id }
        }
    }

    operation {
        startIf { status == ContextStatus.RUNNING && responseEvent.owner.id == principal.id }
        execute {
            permissions += Permission.READ
            permissions += Permission.UPDATE
            permissions += Permission.DELETE
        }
    }
    operation {
        execute {
            permissions += Permission.READ
        }
    }

    operation {
        startIf { status == ContextStatus.RUNNING && useAuth }
        execute {
            if (! permissions.contains(Permission.READ)) {
                errors.add(
                    ErrorValueModel(
                        code = "event-repository-read-error",
                        group = ErrorValueModel.Group.AUTH,
                        level = ErrorValueModel.Level.ERROR,
                        message = "Operation is not permitted"
                    )
                )
                status = ContextStatus.ERROR
                responseEvent = EventModel.NONE
            }
        }
    }

    operation {
        startIf { status == ContextStatus.RUNNING }
        execute {
            try {
                logger.doWithErrorLoggingSusp("event-read") {
                    eventRepository.read(this)
                    status = ContextStatus.FINISHING
                }
            } catch (t: Throwable) {
                status = ContextStatus.FAILING
                errors.add(
                    ErrorValueModel(
                        code = "event-repository-read-error",
                        message = "Внутренняя ошибка, обратитесь к администратору"
                    )
                )
            }
        }
    }


    execute(CompletePipeline)
})