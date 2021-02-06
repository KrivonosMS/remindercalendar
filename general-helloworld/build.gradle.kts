plugins {
    kotlin("jvm") version "1.4.10"
}

group = "ru.krivonos.remindercalendar"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
