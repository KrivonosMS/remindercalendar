package ru.otus.otuskotlin.remindercalendar.pipelines

import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals


class PipelineTest {
    @Test
    fun simplePipeline() {
        val givenOperation = operation<TestContext> {
            execute { a += "c" }
        }
        val givenPipeline = pipeline<TestContext> {
            execute { a = "a" }
            execute(givenOperation)
            operation {
                startIf {
                    a.isNotEmpty()
                }
                execute { a += "b" }
            }
        }
        val givenContext = TestContext()

        runBlockingTest {
            givenPipeline.execute(givenContext)
            assertEquals("acb", givenContext.a)
        }
    }

    @Test
    fun nestedPipeline() {
        val givenOperation = operation<TestContext> {
            execute { a += "c" }
        }
        val givenPipeline = pipeline<TestContext> {
            execute { a = "a" }
            execute(givenOperation)
            pipeline {
                startIf {
                    a.isNotEmpty()
                }
                execute { a += "b" }
            }
        }
        val givenContext = TestContext()

        runBlockingTest {
            givenPipeline.execute(givenContext)
            assertEquals("acb", givenContext.a)
        }
    }

    data class TestContext(
        var a: String = ""
    )
}