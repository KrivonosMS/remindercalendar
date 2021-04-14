package ru.otus.otuskotlin.remindercalendar.ktor.helpers

import kotlinx.serialization.SerializationException
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.remindercalendar.common.backend.model.ErrorValueModel

private val logger = LoggerFactory.getLogger("Throwable.toModel")
fun Throwable.toModel(): ErrorValueModel = when (this) {
    is SerializationException -> ErrorValueModel(message = "Request JSON syntax error: ${this.message}")
    is ClassCastException -> ErrorValueModel(message = "Wrong data sent to the endpoint: ${this.message}")
    else -> {
        logger.error("Unknown exception", this)
        ErrorValueModel(message = "Some exception is thrown: ${this.message}")
    }
}
