plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("com.epages.restdocs-api-spec") version "0.18.2"
}

repositories {
    mavenCentral()
}

openapi3 {
    setServer("https://localhost:8080")
    title = "Sample API"
    description = "this is description of Sample API"
    version = "0.0.1"
    format = "yaml"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation(kotlin("test"))
    testImplementation(project(":documentify-core"))
}

tasks.test {
    useJUnitPlatform()
}