package ru.otus.otuskotlin.remindercalendar.common.mp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


internal class NameValidatorTest {
    @Test
    fun heckValidationNameWhenNameIsEmpty() {
        val nameValidator = NameValidator()

        val res = nameValidator.validate("")

        assertFalse { res.isSuccess() }
        assertEquals(
            "Name must not be empty",
            res.errors.first().message
        )
    }

    @Test
    fun checkValidationAgeWhenAgeOutOfRange() {
        val ageValidator = AgeValidator(2, 5)

        val res = ageValidator.validate(7)

        assertFalse { res.isSuccess() }
        assertEquals(
            "You are too young for this service",
            res.errors.first().message
        )
    }

    @Test
    fun checkChildValidationWhenAgeWrongAndNameIsEmpty() {
        val childValidator = ChildValidator()

        val res = childValidator.validate(
            Child(
                name = "",
                age = 19
            )
        )

        assertFalse { res.isSuccess() }
        assertTrue {
            res.errors.map { it.message }.contains("Name must not be empty")
            res.errors.map { it.message }.contains("You are too young for this service")
        }
    }
}