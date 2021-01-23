package ru.krivonos.remindercalendar.general.dsl

import ru.krivonos.remindercalendar.general.model.UserPermission

@UserDsl
class PermissionConfig {
    private var _permissions: MutableSet<UserPermission> = mutableSetOf()
    val permissions: Set<UserPermission>
        get() = _permissions.toSet()

    fun add(permission: UserPermission) = _permissions.add(permission)

    fun add(permission: String) = add(UserPermission.valueOf(permission.toUpperCase()))

    operator fun UserPermission.unaryPlus() = add(this)
}