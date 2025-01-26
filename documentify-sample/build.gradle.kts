plugins {
    alias(libs.plugins.restdocs.api.spec)
}

repositories {
    mavenCentral()
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