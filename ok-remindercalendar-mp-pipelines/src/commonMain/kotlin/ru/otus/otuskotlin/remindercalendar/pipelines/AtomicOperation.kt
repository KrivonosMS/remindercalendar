package ru.otus.otuskotlin.remindercalendar.pipelines

class AtomicOperation<T>
private constructor(
    private val checkPrecondition: Predicate<T>,
    private val runOperation: Runnable<T>,
    private val handleError: ErrorHandler<T>
) : Operation<T> {
    override suspend fun execute(context: T) {
        try {
            if (checkPrecondition(context)) runOperation(context)
        } catch (throwable: Throwable) {
            handleError(context, throwable)
        }
    }

    @PipelineDsl
    class Builder<T> : OperationBuilder<T> {
        private var checkPrecondition: Predicate<T> = { true }
        private var runOperation: Runnable<T> = {}
        private var handleError: ErrorHandler<T> = { throw it }

        fun startIf(block: Predicate<T>) {
            checkPrecondition = block
        }

        fun execute(block: Runnable<T>) {
            runOperation = block
        }

        fun onError(block: ErrorHandler<T>) {
            handleError = block
        }

        override fun build(): AtomicOperation<T> =
            AtomicOperation(checkPrecondition, runOperation, handleError)
    }
}