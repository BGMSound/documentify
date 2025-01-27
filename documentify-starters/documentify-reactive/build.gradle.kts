dependencies {
    compileOnly(libs.spring.boot.starter.webflux)
    api(libs.spring.restdocs.webtestclient)
    api(libs.restdocs.api.spec)
    api(libs.restdocs.api.spec.webtestclient)
}
