subprojects {
    with(pluginManager) {
        apply(rootProject.libs.plugins.gradle.mavenCentral.publish.get().pluginId)
    }
    dependencies {
        compileOnly(rootProject.libs.spring.boot.starter.test)
        if (project.path != rootProject.projects.documentifyProject.documentifyCore.identityPath.path) {
            api(rootProject.projects.documentifyProject.documentifyCore)
        }
    }
}