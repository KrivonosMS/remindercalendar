package ru.otus.otuskotlin.remindercalendar.pipelines.validation

import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.Predicate


class PipelineValidation<C>(
    private val validations: List<ValidationOperation<C,*>>,
    private val checkPrecondition: Predicate<C> = { true },
    ) : Operation<C> {
    override suspend fun execute(context: C) {
        if (context.checkPrecondition()) {
            validations.forEach {
                it.execute(context)
            }
        }
    }
}
