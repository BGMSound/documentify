dependencies {
    implementation(libs.spring.boot.starter.test)
    implementation(libs.restdocs.api.spec)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    compileOnly(libs.spring.restdocs.mockmvc)
    compileOnly(libs.spring.boot.starter.web)
    compileOnly(libs.spring.boot.starter.webflux)
}