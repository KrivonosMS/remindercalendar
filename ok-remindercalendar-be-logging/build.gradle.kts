val kotlinVersion: String by project
val logbackVersion: String by project
val logbackEncoderVersion: String by project
val logbackKafkaVersion: String by project
val coroutineVersion: String by project
val janinoVersion: String by project
val assertjVersion: String by project
val mockkVersion: String by project

plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")

    // logback
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
    implementation("com.github.danielwegener:logback-kafka-appender:$logbackKafkaVersion")
    api("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.codehaus.janino:janino:$janinoVersion")

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
