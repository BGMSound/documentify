dependencies {
    compileOnly(libs.spring.boot.starter.webflux)
    implementation(libs.spring.restdocs.webtestclient)
    implementation(libs.restdocs.api.spec)
    implementation(libs.restdocs.api.spec.webtestclient)
}