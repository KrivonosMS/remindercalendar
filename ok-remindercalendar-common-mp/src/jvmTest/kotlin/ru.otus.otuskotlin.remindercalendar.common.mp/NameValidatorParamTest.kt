package ru.otus.otuskotlin.remindercalendar.common.mp

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertTrue


internal class NameValidatorParamTest {

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18])
    fun checkChildValidationWhenAgeWrongAndNameIsEmpty(age: Int) {
        val childValidator = ChildValidator()

        val res = childValidator.validate(
            Child(
                name = "Name",
                age = age
            )
        )

        assertTrue { res.isSuccess() }
        assertTrue { res.errors.isEmpty() }
    }
}