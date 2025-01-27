dependencies {
    implementation(rootProject.libs.spring.boot.starter.web)

    testImplementation(rootProject.projects.documentifyStarters.documentifyMvc)
}

openapi3 {
    title = "Sample API"
    description = "This is a sample API documentation."
    version = "0.0.1"
    format = "yaml"
}