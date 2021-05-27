package ru.otus.otuskotlin.remindercalendar.logging

import ch.qos.logback.classic.Logger
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.Marker
import org.slf4j.event.Level
import org.slf4j.event.LoggingEvent
import java.time.Instant


data class LogContext(
    val logger: Logger,

    val loggerId: String = "",
) {
    fun log(
        msg: String = "",
        level: Level = Level.TRACE,
        marker: Marker = DefaultMarker("DEV"),
        e: Throwable? = null,
        data: Any? = null,
        vararg objs: Pair<String, Any>?
    ) = logger.log(
        object : LoggingEvent {
            override fun getThrowable() = e
            override fun getTimeStamp(): Long = Instant.now().toEpochMilli()
            override fun getThreadName(): String = Thread.currentThread().name
            override fun getMessage(): String = msg
            override fun getMarker(): Marker = marker
            override fun getArgumentArray(): Array<out Any> = data
                ?.let { d ->
                    arrayOf(
                        *objs.map { StructuredArguments.keyValue(it?.first, it?.second) }.toTypedArray(),
                        StructuredArguments.keyValue("data", d)
                    )
                        .filterNotNull()
                        .toTypedArray()
                }
                ?: objs.map { StructuredArguments.keyValue(it?.first, it?.second) }.filterNotNull().toTypedArray()

            override fun getLevel(): Level = level
            override fun getLoggerName(): String = logger.name
        }
    )

    suspend fun <T> doWithLoggingSusp(
            logId: String = "",
            marker: Marker = DefaultMarker("DEV"),
            level: Level = Level.INFO,
            block: suspend () -> T,
        ): T = try {
                    val timeStart = Instant.now()
                    log(
                        msg = "$loggerId Entering $logId",
                        level = level,
                        marker = DefaultMarker("START", listOf(marker))
                    )
                    val result = block()
                    val diffTime = Instant.now().toEpochMilli() - timeStart.toEpochMilli()
                    log(
                        msg = "$loggerId Finishing $logId",
                        level = level,
                        marker = DefaultMarker("END", listOf(marker)),
                        objs = arrayOf(Pair("metricHandleTime", diffTime))
                    )
                    result
                } catch (e: Throwable) {
                    log(
                        msg = "$loggerId Failing $logId",
                        level = Level.ERROR,
                        marker = DefaultMarker("ERROR", listOf(marker)),
                        e = e,
                    )
                    throw e
                }

    suspend fun <T> doWithErrorLoggingSusp(
            logId: String = "",
            marker: Marker = DefaultMarker("DEV"),
            needThrow: Boolean = true,
            block: suspend () -> T,
        ): T? = try {
                    val result = block()
                    result
                } catch (e: Throwable) {
                    log(
                        msg = "$loggerId Failing $logId",
                        level = Level.ERROR,
                        marker = DefaultMarker("ERROR", listOf(marker)),
                        e = e,
                    )
                    if (needThrow)
                        throw e
                    else
                        null
                }
}
