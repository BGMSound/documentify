import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.kotlinx.kover)
    java
}

group = extra["project.group"] as String
version = extra["project.version.id"] as String

subprojects {
    group = rootProject.group
    version = rootProject.version
    with(pluginManager) {
        apply(rootProject.libs.plugins.kotlin.jvm.get().pluginId)
        apply(rootProject.libs.plugins.kotlin.plugin.spring.get().pluginId)
    }
    repositories {
        mavenCentral()
    }
    tasks {
        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        withType<KotlinCompile> {
            compilerOptions {
                freeCompilerArgs.add("-Xjsr305=strict")
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
        test {
            useJUnitPlatform()
        }
    }
}

repositories {
    mavenCentral()
}

kover {
    merge {
        projects(
            rootProject.projects.documentifyCore.identityPath.path,
            rootProject.projects.documentifyStarters.documentifyMvc.identityPath.path,
            rootProject.projects.documentifyStarters.documentifyReactive.identityPath.path,
            rootProject.projects.documentifySample.mvcSample.identityPath.path,
            rootProject.projects.documentifySample.reactiveSample.identityPath.path
        )
    }
    reports {
        filters {
            excludes.classes.add("**.sample.**")
        }
    }
}