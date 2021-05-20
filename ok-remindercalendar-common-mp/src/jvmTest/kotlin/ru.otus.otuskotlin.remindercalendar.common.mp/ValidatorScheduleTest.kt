package ru.otus.otuskotlin.remindercalendar.common.mp

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorSchedule
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ValidatorScheduleTest {
    @Test
    fun checkValidatorSchedulesWhenTimeIsMin() {
        val validatorSchedule = ValidatorSchedule()

        val res = validatorSchedule.validate(LocalDateTime.MIN)

        assertFalse { res.isSuccess() }
        assertEquals(
            "Schedule time must be given",
            res.errors.first().message
        )
    }

    @Test
    fun checkValidatorSchedulesWhenTimeIsNow() {
        val validatorSchedule = ValidatorSchedule()

        val res = validatorSchedule.validate(LocalDateTime.now())

        Assertions.assertTrue { res.isSuccess() }
        Assertions.assertTrue(res.errors.isEmpty())
    }
}