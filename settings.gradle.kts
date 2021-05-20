rootProject.name = "otuskotlin-202012-remindercalendar-km"

pluginManagement {
    val kotlinVersion: String by settings
    val bmuschkoVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("js") version kotlinVersion apply false
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
    }
}

include("ok-remindercalendar-common-be")
include("ok-remindercalendar-common-mp")
include("ok-remindercalendar-transport-main-mp")
include("ok-remindercalendar-be-mappers-mp")
include("ok-remindercalendar-be-app-ktor")
include("ok-remindercalendar-mp-pipelines")
include("ok-remindercalendar-be-business-logic")
include("ok-remindercalendar-mp-pipelines-validation")
include("ok-remindercalendar-be-repository-inmemory")
include("ok-remindercalendar-be-repository-sql")
