package ru.otus.otuskotlin.remindercalendar.logging

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test


internal class LoggerTest {

    @Test
    fun loggerInit() {
        runBlocking{
            val logger = logger(this::class.java)
            logger.doWithLoggingSusp(logId = "test-logger") {
                println("Some action")
            }
        }
    }
}