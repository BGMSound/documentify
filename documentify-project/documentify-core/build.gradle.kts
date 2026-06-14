dependencies {
    implementation(libs.restdocs.api.spec)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    compileOnly(libs.spring.boot.starter.test)
    compileOnly(libs.spring.restdocs.webtestclient)
    compileOnly(libs.spring.boot.starter.web)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.restdocs.webtestclient)
}