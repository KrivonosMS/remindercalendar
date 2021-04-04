package ru.otus.otuskotlin.remindercalendar.common.mp

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.otus.otuskotlin.remindercalendar.common.backend.model.FrequencyModel
import ru.otus.otuskotlin.remindercalendar.common.mp.test.ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorFrequency
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ValidatorFrequencyTest {
    @Test
    fun checkValidationFrequencyWhenIsNone() {
        val validatorFrequency = ValidatorFrequency()

        val res = validatorFrequency.validate(FrequencyModel.NONE)

        assertFalse { res.isSuccess() }
        assertEquals(
            "Frequency period must be given",
            res.errors.first().message
        )
    }

    @EnumSource(
        value = FrequencyModel::class,
                names = ["NONE"],
        mode = EnumSource.Mode.EXCLUDE
    )
    @ParameterizedTest
    fun checkValidationFrequencyWhenIsNotNone(frequency: FrequencyModel) {
        val validatorFrequency = ValidatorFrequency()

        val res = validatorFrequency.validate(frequency)

        assertTrue { res.isSuccess() }
        assertTrue(res.errors.isEmpty())
    }
}