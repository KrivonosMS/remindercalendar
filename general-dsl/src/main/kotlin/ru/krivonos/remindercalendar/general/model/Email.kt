package ru.krivonos.remindercalendar.general.model

inline class Email(val email: String) {
    companion object {
        val NONE = Email("")
    }
}
