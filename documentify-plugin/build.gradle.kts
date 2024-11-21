plugins {
    id("com.gradle.plugin-publish") version "1.2.0"
    id("com.epages.restdocs-api-spec") version "0.18.2"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(project(":documentify-core"))
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