package ru.otus.otuskotlin.remindercalendar.common.mp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse


internal class NameValidatorTest {
    @Test
    fun checkValidationFieldWhenIsEmpty() {
        val validatorStringFieldNotEmpty = ValidatorStringFieldNotEmpty()

        val res = validatorStringFieldNotEmpty.validate("")

        assertFalse { res.isSuccess() }
        assertEquals(
            "Field must not be empty or null",
            res.errors.first().message
        )
    }

    @Test
    fun heckValidationFieldWhenIsBlank() {
        val validatorStringFieldNotEmpty = ValidatorStringFieldNotEmpty()

        val res = validatorStringFieldNotEmpty.validate("  ")

        assertFalse { res.isSuccess() }
        assertEquals(
            "Field must not be empty or null",
            res.errors.first().message
        )
    }

    @Test
    fun heckValidationFieldWhenIsNull() {
        val validatorStringFieldNotEmpty = ValidatorStringFieldNotEmpty()

        val res = validatorStringFieldNotEmpty.validate(null)

        assertFalse { res.isSuccess() }
        assertEquals(
            "Field must not be empty or null",
            res.errors.first().message
        )
    }
}