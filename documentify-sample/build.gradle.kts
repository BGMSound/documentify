plugins {
    id("io.github.bgmsound.documentify") version "1.0.0" apply false
}

subprojects {
    with(pluginManager) {
        apply("io.github.bgmsound.documentify")
    }
    dependencies {
        testImplementation(rootProject.libs.spring.boot.starter.test)
        testImplementation(rootProject.libs.mockk)
    }
}