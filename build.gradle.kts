plugins {
    kotlin("jvm")
}

group = "kr.bgmsound"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
}

subprojects {
    group = rootProject.group
    version = rootProject.version
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    kotlin {
        jvmToolchain(17)
    }
}

kotlin {
    jvmToolchain(17)
}