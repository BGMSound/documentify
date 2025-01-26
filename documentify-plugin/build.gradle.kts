plugins {
    id("com.gradle.plugin-publish") version "1.2.0"
    `java-gradle-plugin`
    `maven-publish`
}

dependencies {
    implementation(gradleApi())
    implementation(projects.documentifyCore)
    testImplementation(kotlin("test"))
}

gradlePlugin {
    plugins {
        register("documentify") {
            id = "io.github.bgmsound.documentify"
            implementationClass = "io.github.bgmsound.documentify.plugin.DocumentifyPlugin"
            displayName = "Documentify"
            description = "easy and powerful API documentation tool for spring restdocs"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}