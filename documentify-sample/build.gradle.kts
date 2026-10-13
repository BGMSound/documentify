plugins {
    alias(libs.plugins.restdocs.api.spec) apply false
}

subprojects {
    with(pluginManager) {
        apply(rootProject.libs.plugins.restdocs.api.spec.get().pluginId)
    }
    dependencies {
        implementation(rootProject.libs.jackson.kotlin)
        testImplementation(rootProject.libs.spring.boot.starter.test)
        testImplementation(rootProject.libs.spring.restdocs.webtestclient)
        testImplementation(rootProject.libs.mockk)
    }
}