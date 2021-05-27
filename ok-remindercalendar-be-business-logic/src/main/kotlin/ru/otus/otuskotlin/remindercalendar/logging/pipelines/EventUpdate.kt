package ru.otus.otuskotlin.remindercalendar.logging.pipelines

import ru.otus.otuskotlin.remindercalendar.logging.helpers.validation
import ru.otus.otuskotlin.remindercalendar.logging.operations.CompletePipeline
import ru.otus.otuskotlin.remindercalendar.logging.operations.InitializePipeline
import ru.otus.otuskotlin.remindercalendar.logging.operations.QuerySetWorkMode
import ru.otus.otuskotlin.remindercalendar.logging.operations.stubs.EventUpdateStub
import ru.otus.otuskotlin.remindercalendar.common.backend.context.Context
import ru.otus.otuskotlin.remindercalendar.common.backend.context.ContextStatus
import ru.otus.otuskotlin.remindercalendar.common.backend.model.ErrorValueModel
import ru.otus.otuskotlin.remindercalendar.common.backend.model.FrequencyModel
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorStringFieldNotEmpty
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorFrequency
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorSchedule
import ru.otus.otuskotlin.remindercalendar.logging.logger
import ru.otus.otuskotlin.remindercalendar.pipelines.operation
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline
import java.time.LocalDateTime

private val logger = logger(EventUpdate::class.java)

object EventUpdate : Operation<Context> by pipeline({
    execute(InitializePipeline)

    execute(QuerySetWorkMode)

    execute(EventUpdateStub)

    validation {
        validate<String?> {
            validator(ValidatorStringFieldNotEmpty("You must provide schedule event id"))
            on { requestEventId.id }
        }
        validate<String?> {
            validator(ValidatorStringFieldNotEmpty("You must provide non-empty name for schedule event"))
            on { requestEvent.name }
        }
        validate<String?> {
            validator(ValidatorStringFieldNotEmpty("You must provide non-empty mobile number for schedule event"))
            on { requestEvent.mobile }
        }
        validate<String?> {
            validator(ValidatorStringFieldNotEmpty("You must provide user for schedule event"))
            on { requestEvent.userId.id }
        }
        validate<FrequencyModel> {
            validator(ValidatorFrequency("You must provide frequency for schedule event"))
            on { requestEvent.frequency }
        }
        validate<LocalDateTime> {
            validator(ValidatorSchedule("You must provide schedule time for schedule event"))
            on { requestEvent.startSchedule }
        }

    }

    operation {
        startIf { status == ContextStatus.RUNNING }
        execute {
            try {
                logger.doWithErrorLoggingSusp("event-update") {
                    eventRepository.update(this)
                    status = ContextStatus.FINISHING
                }
            } catch (t: Throwable) {
                status = ContextStatus.FAILING
                errors.add(
                    ErrorValueModel(
                        code = "event-repository-update-error",
                        message = "Внутренняя ошибка, обратитесь к администратору"
                    )
                )
            }
        }
    }

    execute(CompletePipeline)
})