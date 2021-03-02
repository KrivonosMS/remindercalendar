plugins {
    kotlin("jvm")
}

val assertjVersion: String by project
val mockkVersion: String by project
val junitBomVersion: String by project

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(platform("org.junit:junit-bom:$junitBomVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")

    implementation(project(":ok-remindercalendar-common-be"))
    implementation(project(":ok-remindercalendar-transport-main-mp"))
}

tasks {
    test {
        useJUnitPlatform()
    }
}