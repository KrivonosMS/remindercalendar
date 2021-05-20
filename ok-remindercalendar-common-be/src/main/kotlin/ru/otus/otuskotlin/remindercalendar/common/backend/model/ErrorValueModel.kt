package ru.otus.otuskotlin.remindercalendar.common.backend.model

data class ErrorValueModel(
    val code: String = "",
    val field: String = "",
    val message: String = "",
    val group: Group = Group.NONE,
    val level: Level = Level.ERROR,
) {

    enum class Group(val alias: String) {
        NONE(""),
        SERVER("internal-server"),
        AUTH("auth"),
    }

    enum class Level(val weight: Int) {
        FATAL(90),
        ERROR(70),
        WARNING(40),
        INFO(20);

        val isError: Boolean
            get() = weight >= ERROR.weight

        val isWarning: Boolean
            get() = this == WARNING
    }
}
