dependencies {
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.reactor)

    testImplementation(rootProject.projects.documentifyStarters.documentifyStarterReactive)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(kotlin("test"))
}

openapi3 {
    title = "Reactive Sample API"
    description = "This is a reactive sample API documentation."
    version = "0.0.1"
    format = "yaml"
}