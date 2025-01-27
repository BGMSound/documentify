dependencies {
    compileOnly(libs.spring.boot.starter.web)
    api(libs.spring.restdocs.mockmvc)
    api(libs.spring.restdocs.restassured)
    api(libs.restassured.mockmvc)
    api(libs.restdocs.api.spec)
    api(libs.restdocs.api.spec.mockmvc)
    api(libs.restdocs.api.spec.restassured)
}