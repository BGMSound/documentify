plugins {
    id("maven-publish")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-test")
    api("org.springframework.restdocs:spring-restdocs-mockmvc")
    api("org.springframework.restdocs:spring-restdocs-restassured")
    api("io.rest-assured:spring-mock-mvc:5.5.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")
    api("com.epages:restdocs-api-spec-mockmvc:0.18.2")
    api("com.epages:restdocs-api-spec-restassured:0.18.2")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "io.github.bgmsound"
            artifactId = "documentify-core"
            version = rootProject.version.toString()
            from(components["java"])
        }
    }
}