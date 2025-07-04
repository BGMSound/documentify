subprojects {
    with(pluginManager) {
        apply(rootProject.libs.plugins.gradle.mavenCentral.publish.get().pluginId)
    }
    dependencies {
        compileOnly(rootProject.libs.spring.boot.starter.test)
        api(rootProject.projects.documentifyProject.documentifyCore)
    }
}