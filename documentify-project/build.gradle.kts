subprojects {
    with(pluginManager) {
        apply(rootProject.libs.plugins.gradle.mavenCentral.publish.get().pluginId)
    }
    if (project.path == rootProject.projects.documentifyProject.documentifyGradlePlugin.path) return@subprojects
    dependencies {
        compileOnly(rootProject.libs.spring.boot.starter.test)
        if (project.path != rootProject.projects.documentifyProject.documentifyCore.path) {
            api(rootProject.projects.documentifyProject.documentifyCore)
        }
    }
}