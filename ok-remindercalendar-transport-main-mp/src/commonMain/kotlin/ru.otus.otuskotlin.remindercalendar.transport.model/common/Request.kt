package ru.otus.otuskotlin.remindercalendar.transport.model.common

interface Request {
    val requestId: String?
    val onResponseId: String?
    val startTime: String?
    val debug: Debug?
}