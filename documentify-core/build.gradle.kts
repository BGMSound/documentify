plugins {
    id("maven-publish")
}

dependencies {
    implementation(spring.test)
    implementation(spring.restdocs.mockmvc)
    implementation(spring.restdocs.assured)
    implementation(lib.rest.assured)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("com.epages:restdocs-api-spec-mockmvc:0.18.2")
    implementation("com.epages:restdocs-api-spec-restassured:0.18.2")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "kr.bgmsound"
            artifactId = "documentify-core"
            version = rootProject.version.toString()
            from(components["java"])
        }
    }
}