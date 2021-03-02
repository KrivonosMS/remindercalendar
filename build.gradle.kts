plugins {
    kotlin("multiplatform") apply false
    kotlin("jvm") apply false
    kotlin("js") apply false
}

val projectVersion: String by project
val groupName: String by project

group = "ru.krivonos.remindercalendar"
version = projectVersion