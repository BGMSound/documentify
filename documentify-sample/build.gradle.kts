plugins {
    alias(libs.plugins.restdocs.api.spec)
    jacoco
}

subprojects {
    apply(plugin = "jacoco")
    jacoco {
        toolVersion = property("jacoco.version").toString()
    }
    dependencies {
        implementation(rootProject.libs.spring.boot.starter.test)
        testImplementation(rootProject.libs.mockk)
    }
}