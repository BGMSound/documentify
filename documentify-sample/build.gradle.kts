plugins {
    alias(libs.plugins.restdocs.api.spec)
    jacoco
}

jacoco {
    toolVersion = "0.8.12"
}

tasks {
    jacocoTestReport {
        dependsOn("test")

        val coreModulePath = rootProject.projects.documentifyCore.identityPath.path
        val coreModuleClasses = project(coreModulePath).sourceSets.getByName("main").output.classesDirs
        val coreModuleSources = project(coreModulePath).sourceSets.getByName("main").allSource
        additionalClassDirs.setFrom(files(coreModuleClasses))
        sourceDirectories.setFrom(files(coreModuleSources))

        reports {
            html.required.set(true)
            xml.required.set(true)
            csv.required.set(false)
        }
        finalizedBy("jacocoTestCoverageVerification")
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

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.test)
    testImplementation(libs.mockk)
    testImplementation(projects.documentifyCore)
}

tasks.test {
    useJUnitPlatform()
}