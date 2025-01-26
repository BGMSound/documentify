import com.vanniktech.maven.publish.SonatypeHost
import java.net.HttpURLConnection
import java.net.URL
import java.time.Year

plugins {
    id("com.vanniktech.maven.publish") version "0.28.0"
    signing
    `maven-publish`
}

fun property(key: String): String {
    return extra[key]?.toString() ?: throw IllegalArgumentException("property with $key not found")
}

dependencies {
    implementation(libs.spring.boot.starter.test)
    compileOnly(libs.spring.boot.starter.web)
    api(libs.spring.restdocs.mockmvc)
    api(libs.spring.restdocs.restassured)
    api(libs.restassured.mockmvc)
    api(libs.jackson.databind)
    api(libs.jackson.datatype.jsr310)
    api(libs.restdocs.api.spec.mockmvc)
    api(libs.restdocs.api.spec.restassured)
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    coordinates(
        groupId = property("project.group"),
        artifactId = "${property("project.name")}-core",
        version = property("project.version.id")
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

tasks.register("checkVersionTask") {
    doLast {
        checkVersion()
    }
}

tasks.named("publishAllPublicationsToMavenCentralRepository") {
    dependsOn("checkVersionTask")
}

fun checkVersion() {
    val version = property("project.version.id")
    val group = property("project.group").replace(".", "/")
    val name = property("project.name")

    val mavenCentralUrl = "https://repo1.maven.org/maven2/${group}/${name}-core/${version}/"
    val url = URL(mavenCentralUrl)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    if (connection.responseCode != 404) {
        throw IllegalArgumentException("version $version already exists")
    }
}