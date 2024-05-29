plugins {
    id("maven-publish")
}

dependencies {
    implementation(spring.test)
    implementation(spring.restdocs.mockmvc)
    implementation(spring.restdocs.assured)
    implementation(lib.rest.assured)
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