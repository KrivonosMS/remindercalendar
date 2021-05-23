val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val ktorKafkaVersion: String by project
val kafkaVersion: String by project
val assertjVersion: String by project
val mockkVersion: String by project
val junitBomVersion: String by project

plugins {
    application
    kotlin("jvm")
    id("com.bmuschko.docker-java-application")
}

group = rootProject.group
version = rootProject.version

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

docker {
    javaApplication {
        baseImage.set("adoptopenjdk/openjdk11:alpine-jre")
        maintainer.set("(c) KrivonosMS")
        ports.set(listOf(8080))
        val imageName = project.name
        images.set(listOf(
            "$imageName:${project.version}",
            "$imageName:latest"
        ))
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
    }
}

dependencies {
    implementation(project(":ok-remindercalendar-common-be"))
    implementation(project(":ok-remindercalendar-transport-main-mp"))
    implementation(project(":ok-remindercalendar-be-mappers-mp"))
    implementation(project(":ok-remindercalendar-be-business-logic"))
    implementation(project(":ok-remindercalendar-be-repository-inmemory"))
    implementation(project(":ok-remindercalendar-be-repository-sql"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("com.github.Datana-company:ktor-kafka:$ktorKafkaVersion")
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")

}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
