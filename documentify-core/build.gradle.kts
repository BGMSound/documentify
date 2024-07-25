import com.vanniktech.maven.publish.SonatypeHost
import java.time.Year

plugins {
    id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.28.0"
    id("signing")
}

fun property(key: String): String {
    return extra[key]?.toString() ?: throw IllegalArgumentException("property with $key not found")
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

signing {
    extra["mavenCentralUsername"] = System.getenv("MAVEN_CENTRAL_USERNAME")
    extra["mavenCentralPassword"] = System.getenv("MAVEN_CENTRAL_PASSWORD")

    useGpgCmd()
    sign(publishing.publications)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    coordinates(
        groupId = property("project.group"),
        artifactId = "${property("project.name")}-core",
        version = property("project.version")
    )

    pom {
        name = property("project.name")
        description = property("project.description")
        inceptionYear = "${Year.now().value}"
        url = property("project.url")
        licenses {
            license {
                name = property("project.license")
                url = property("project.license.url")
                distribution = property("project.license.url")
            }
        }
        developers {
            developer {
                id = property("project.developer.id")
                name = property("project.developer.name")
                url = property("project.developer.url")
            }
        }
        scm {
            url = property("project.developer.url")
            connection = "scm:git:git://github.com/${property("project.developer.id")}"
            developerConnection = "scm:git:ssh://git@github.com/${property("project.developer.id")}"
        }
    }
}