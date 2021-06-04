plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    val assertjVersion: String by project
    val mockkVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":ok-remindercalendar-common-mp"))
    implementation(project(":ok-remindercalendar-common-be"))
    implementation(project(":ok-remindercalendar-mp-pipelines"))
    implementation(project(":ok-remindercalendar-mp-pipelines-validation"))
    implementation(project(":ok-remindercalendar-be-logging"))

    testImplementation(kotlin("test-junit5"))
    testImplementation(platform("org.junit:junit-bom:5.7.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
