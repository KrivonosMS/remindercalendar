package ru.otus.otuskotlin.remindercalendar.common.backend.model

data class PrincipalModel(
    val id: UserIdModel = UserIdModel.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: List<UserGroups> = emptyList()
) {
    companion object {
        val NONE = PrincipalModel()
    }
}
