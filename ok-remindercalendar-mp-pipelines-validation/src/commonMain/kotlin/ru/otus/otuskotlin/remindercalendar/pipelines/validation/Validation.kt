package ru.otus.otuskotlin.remindercalendar.pipelines.validation

import ru.otus.otuskotlin.remindercalendar.pipelines.Pipeline


fun <C> Pipeline.Builder<C>.validation(block: ValidationBuilder<C>.() -> Unit) {
    execute(ValidationBuilder<C>().apply(block).build())
}
