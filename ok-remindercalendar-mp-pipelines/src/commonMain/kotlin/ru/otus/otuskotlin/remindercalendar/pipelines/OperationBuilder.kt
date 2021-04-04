package ru.otus.otuskotlin.remindercalendar.pipelines

interface OperationBuilder<T> {
    fun build(): Operation<T>
}
