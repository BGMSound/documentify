import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    java
    jacoco
}

jacoco {
    toolVersion = "0.8.12"
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

tasks {
    jacocoTestReport {
        val mvcSampleModulePath = rootProject.projects.documentifySample.mvcSample.identityPath.path
        val reactiveSampleModulePath = rootProject.projects.documentifySample.reactiveSample.identityPath.path
        dependsOn(
            "${mvcSampleModulePath}:test",
            "${reactiveSampleModulePath}:test"
        )

        val coreModulePath = rootProject.projects.documentifyCore.identityPath.path
        val coreModuleClasses = project(coreModulePath).sourceSets.getByName("main").output.classesDirs
        val coreModuleSources = project(coreModulePath).sourceSets.getByName("main").allSource

        additionalClassDirs.setFrom(files(coreModuleClasses))
        sourceDirectories.setFrom(files(coreModuleSources))

        executionData.setFrom(
            project(mvcSampleModulePath).layout.buildDirectory.file("jacoco/test.exec"),
            project(reactiveSampleModulePath).layout.buildDirectory.file("jacoco/test.exec")
        )

        reports {
            html.required.set(true)
            xml.required.set(true)
            csv.required.set(false)
        }
        finalizedBy(
            "${mvcSampleModulePath}:jacocoTestCoverageVerification",
            "${reactiveSampleModulePath}:jacocoTestCoverageVerification"
        )
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) {
                exclude("**/documentify/sample/**")
            }
        }))
    }
    jacocoTestCoverageVerification {
        dependsOn("jacocoTestReport")
        violationRules {
            rule {
                element = "CLASS"
                excludes = listOf("*.documentify.sample.*")
            }
        }
    }
}