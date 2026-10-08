dependencies {
    implementation(libs.restdocs.api.spec) {
        exclude(group = "org.springframework.boot")
    }
    api(libs.jackson.databind)
    compileOnly(libs.spring.boot.starter.test)
    compileOnly(libs.spring.restdocs.webtestclient)
    compileOnly(libs.spring.boot.starter.web)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.restdocs.webtestclient)
}