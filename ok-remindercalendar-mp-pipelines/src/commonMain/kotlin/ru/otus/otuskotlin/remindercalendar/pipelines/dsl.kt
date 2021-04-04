package ru.otus.otuskotlin.remindercalendar.pipelines

inline fun <T> pipeline(block: Pipeline.Builder<T>.() -> Unit): Pipeline<T> =
    Pipeline.Builder<T>().apply(block).build()

inline fun <T> operation(block: AtomicOperation.Builder<T>.() -> Unit): AtomicOperation<T> =
    AtomicOperation.Builder<T>().apply(block).build()

inline fun <T> Pipeline.Builder<T>.pipeline(block: Pipeline.Builder<T>.() -> Unit) {
    execute(Pipeline.Builder<T>().apply(block).build())
}

inline fun <T> Pipeline.Builder<T>.operation(block: AtomicOperation.Builder<T>.() -> Unit) {
    execute(AtomicOperation.Builder<T>().apply(block).build())
}