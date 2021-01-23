package ru.krivonos.remindercalendar.general.dsl

import ru.krivonos.remindercalendar.general.model.*
import java.time.LocalDate

@UserDsl
class UserConfig {
    private var id: UserId = UserId.NONE
    private var name_f: String = ""
    private var name_s: String = ""
    private var name_l: String = ""
    private var dob: LocalDate = LocalDate.MIN
    private var email: Email = Email.NONE
    private var phone: Phone = Phone.NONE
    private var permissions: MutableSet<UserPermission> = mutableSetOf()

    fun name(block: NameConfig.() -> Unit) {
        val nameConfig = NameConfig().apply(block)
        name_f = nameConfig.first
        name_s = nameConfig.second
        name_l = nameConfig.last
    }

    fun birth(block: BirthConfig.() -> Unit) {
        val birthConfig = BirthConfig().apply(block)
        dob = LocalDate.from(birthConfig.date)
    }

    fun contacts(block: ContactConfig.() -> Unit) {
        val contactConfig = ContactConfig().apply(block)
        phone = Phone(contactConfig.phone)
        email = Email(contactConfig.email)
    }

    fun permissions(block: PermissionConfig.() -> Unit) {
        val permissionConfig = PermissionConfig().apply(block)
        permissions.addAll(permissionConfig.permissions)
    }

    fun builder() = UserModel(
            id = id,
            fname = name_f,
            sname = name_s,
            lname = name_l,
            dob = dob,
            email = email,
            phone = phone,
            permissions =permissions
    )
}

fun user(block: UserConfig.() -> Unit) = UserConfig().apply(block).builder()