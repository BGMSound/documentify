plugins {
    jacoco
}

jacoco {
    toolVersion = "0.8.12"
}

dependencies {
    implementation(rootProject.libs.spring.boot.starter.web)
}