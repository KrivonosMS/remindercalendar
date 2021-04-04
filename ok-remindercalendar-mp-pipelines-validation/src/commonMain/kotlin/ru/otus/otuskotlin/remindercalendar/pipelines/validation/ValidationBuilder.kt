package ru.otus.otuskotlin.remindercalendar.pipelines.validation

import ru.otus.otuskotlin.remindercalendar.common.mp.ValidationResult
import ru.otus.otuskotlin.remindercalendar.pipelines.Operation
import ru.otus.otuskotlin.remindercalendar.pipelines.OperationBuilder
import ru.otus.otuskotlin.remindercalendar.pipelines.PipelineDsl
import ru.otus.otuskotlin.remindercalendar.pipelines.Predicate


@PipelineDsl
class ValidationBuilder<C>: OperationBuilder<C> {
    private var checkPrecondition: Predicate<C> = { true }
    private var errorHandler: C.(ValidationResult) -> Unit = {}
    private val validators: MutableList<ValidationOperation<C,*>> = mutableListOf()

    fun startIf(block: Predicate<C>) {
        checkPrecondition = block
    }

    fun errorHandler(block: C.(ValidationResult) -> Unit) {
        errorHandler = block
    }

    fun <T> validate(block: ValidationOperationBuilder<C,T>.() -> Unit) {
        val builder = ValidationOperationBuilder<C,T>(errorHandler).apply(block)
        validators.add(builder.build())
    }

    override fun build(): Operation<C> = PipelineValidation(validators)

}
