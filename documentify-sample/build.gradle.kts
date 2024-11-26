plugins {
    kotlin("plugin.spring")
    id("com.epages.restdocs-api-spec") version "0.18.2"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation(project(":documentify-core"))
}

tasks.test {
    useJUnitPlatform()
}