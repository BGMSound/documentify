dependencies {
    compileOnly(libs.spring.boot.starter.web)
    implementation(libs.spring.restdocs.mockmvc)
    implementation(libs.spring.restdocs.restassured)
    implementation(libs.restassured.mockmvc)
    implementation(libs.restdocs.api.spec)
    implementation(libs.restdocs.api.spec.mockmvc)
    implementation(libs.restdocs.api.spec.restassured)
}