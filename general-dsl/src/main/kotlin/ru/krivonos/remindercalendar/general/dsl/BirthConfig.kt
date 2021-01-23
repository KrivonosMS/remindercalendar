package ru.krivonos.remindercalendar.general.dsl

import java.time.LocalDate

@UserDsl
class BirthConfig {
    var date: LocalDate = LocalDate.MIN
}