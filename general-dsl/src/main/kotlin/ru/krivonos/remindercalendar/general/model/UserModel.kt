package ru.krivonos.remindercalendar.general.model

import java.time.LocalDate

data class UserModel (
    var id: UserId = UserId.NONE,
    var fname: String = "",
    var sname: String = "",
    var lname: String = "",
    var dob: LocalDate = LocalDate.MIN,
    var email: Email = Email.NONE,
    var phone: Phone = Phone.NONE,
    var permissions: MutableSet<UserPermission> = mutableSetOf()
)