plugins {
    alias(libs.plugins.restdocs.api.spec)
    jacoco
}

jacoco {
    toolVersion = "0.8.12"
}

tasks {
    jacocoTestReport {
        dependsOn(
            "${rootProject.projects.documentifySample.mvcSample.identityPath.path}:test",
            "${rootProject.projects.documentifySample.reactiveSample.identityPath.path}:test"
        )

        val coreModulePath = rootProject.projects.documentifyCore.identityPath.path
        val coreModuleClasses = project(coreModulePath).sourceSets.getByName("main").output.classesDirs
        val coreModuleSources = project(coreModulePath).sourceSets.getByName("main").allSource

        additionalClassDirs.setFrom(files(coreModuleClasses))
        sourceDirectories.setFrom(files(coreModuleSources))

        executionData.setFrom(
            project(":documentify-sample:mvc-sample").layout.buildDirectory.file("jacoco/test.exec"),
            project(":documentify-sample:reactive-sample").layout.buildDirectory.file("jacoco/test.exec")
        )

        reports {
            html.required.set(true)
            xml.required.set(true)
            csv.required.set(false)
        }
        finalizedBy(
            "${rootProject.projects.documentifySample.mvcSample.identityPath.path}:jacocoTestCoverageVerification",
            "${rootProject.projects.documentifySample.reactiveSample.identityPath.path}:jacocoTestCoverageVerification"
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

subprojects {
    dependencies {
        implementation(rootProject.libs.spring.boot.starter.test)
        testImplementation(rootProject.libs.mockk)
        testImplementation(rootProject.projects.documentifyCore)
    }
}