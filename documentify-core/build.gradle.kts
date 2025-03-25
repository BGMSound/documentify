dependencies {
    implementation(libs.spring.boot.starter.test)
    implementation(libs.restdocs.api.spec)
    compileOnly(libs.spring.restdocs.mockmvc)
    compileOnly(libs.spring.boot.starter.web)
    compileOnly(libs.spring.boot.starter.webflux)
    api(libs.jackson.databind)
    api(libs.jackson.datatype.jsr310)
}