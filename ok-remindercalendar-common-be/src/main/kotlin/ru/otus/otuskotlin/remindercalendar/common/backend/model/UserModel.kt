package ru.otus.otuskotlin.remindercalendar.common.backend.model

data class UserModel(
    val id: UserIdModel = UserIdModel.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
) {
    companion object {
        val NONE = UserModel()
    }
}
