subprojects {
    dependencies {
        compileOnly(rootProject.libs.spring.boot.starter.test)
        api(rootProject.projects.documentifyCore)
    }
}