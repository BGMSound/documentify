plugins {
    id("io.github.bgmsound.documentify") version "1.2.5" apply false
}

subprojects {
    with(pluginManager) {
        apply("io.github.bgmsound.documentify")
    }
    dependencies {
        implementation(rootProject.libs.jackson.kotlin)
        testImplementation(rootProject.libs.spring.boot.starter.test)
        testImplementation(rootProject.libs.mockk)
    }
}