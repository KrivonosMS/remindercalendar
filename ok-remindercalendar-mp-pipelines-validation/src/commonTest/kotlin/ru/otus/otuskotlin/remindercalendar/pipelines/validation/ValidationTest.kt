package ru.otus.otuskotlin.remindercalendar.pipelines.validation

import ru.otus.otuskotlin.remindercalendar.common.mp.Error
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidationResult
import ru.otus.otuskotlin.remindercalendar.common.mp.ValidatorStringFieldNotEmpty
import ru.otus.otuskotlin.remindercalendar.pipelines.pipeline
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ValidationTest {

    @Test
    fun pipelineValidation() {
        val pl = pipeline<TestContext> {

            validation {
                errorHandler { v: ValidationResult ->
                    if (v.isSuccess()) return@errorHandler
                    errors.addAll(v.errors)
                }

                validate<String?> { validator(ValidatorStringFieldNotEmpty()); on { y } }
            }
        }
        runBlockingTest {
            val ctx = TestContext()
            pl.execute(ctx)
            assertEquals(1, ctx.errors.size)
        }
    }

    data class TestContext(
        val y: String = "",
        val errors: MutableList<Error> = mutableListOf()
    )
}

