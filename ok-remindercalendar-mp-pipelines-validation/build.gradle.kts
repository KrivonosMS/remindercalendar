plugins {
    kotlin("multiplatform")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    js {
        browser { }
        nodejs { }
    }
    jvm { }

    sourceSets {
        val coroutineVersion: String by project
        val jupiterVersion: String by project
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":ok-remindercalendar-common-mp"))
                implementation(project(":ok-remindercalendar-mp-pipelines"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
            }
        }
    }
}
