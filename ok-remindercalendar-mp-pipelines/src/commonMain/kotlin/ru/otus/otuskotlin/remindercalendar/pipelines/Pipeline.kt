package ru.otus.otuskotlin.remindercalendar.pipelines

class Pipeline<T>
private constructor(
    private val operations: Collection<Operation<T>>,
    private val checkPrecondition: Predicate<T>,
    private val handleError: ErrorHandler<T>
) : Operation<T> {
    override suspend fun execute(context: T) {
        try {
            if (checkPrecondition(context)) operations.forEach { it.execute(context) }
        } catch (throwable: Throwable) {
            handleError(context, throwable)
        }
    }

    @PipelineDsl
    class Builder<T> : OperationBuilder<T> {
        private val operations: MutableList<Operation<T>> = mutableListOf()
        private var checkPrecondition: Predicate<T> = { true }
        private var handleError: ErrorHandler<T> = { throw it }

        fun execute(operation: Operation<T>) {
            operations.add(operation)
        }

        fun execute(block: Runnable<T>) {
            execute(AtomicOperation.Builder<T>().apply { execute(block) }.build())
        }

        fun startIf(block: Predicate<T>) {
            checkPrecondition = block
        }

        fun onError(block: ErrorHandler<T>) {
            handleError = block
        }

        override fun build(): Pipeline<T> =
            Pipeline(operations, checkPrecondition, handleError)
    }
}