import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    id("io.spring.dependency-management") version "1.1.5"
}

group = extra["project.group"] as String
version = extra["project.version.id"] as String

repositories {
    mavenCentral()
}

subprojects {
    group = rootProject.group
    version = rootProject.version
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.1")
        }
    }

    tasks {
        kotlin {
            compilerOptions {
                freeCompilerArgs.add("-Xjsr305=strict")
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
}