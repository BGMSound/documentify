import com.vanniktech.maven.publish.SonatypeHost
import java.time.Year

plugins {
    signing
    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.28.0"
}

dependencies {
    implementation(libs.spring.boot.starter.test)
    implementation(libs.restdocs.api.spec)
    compileOnly(libs.spring.restdocs.mockmvc)
    compileOnly(libs.spring.boot.starter.web)
    compileOnly(libs.spring.boot.starter.webflux)
    api(libs.jackson.databind)
    api(libs.jackson.datatype.jsr310)
}

signing {
    val gpgSecret = project.findProperty("gpg.secret").toString()
    val gpgPassphrase = project.findProperty("gpg.passphrase").toString()

    useInMemoryPgpKeys(gpgSecret, gpgPassphrase)
    sign(publishing.publications)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    val projectGroup = property("project.group").toString()
    val projectName = property("project.name").toString()
    val projectVersion = property("project.version.id").toString()
    val projectDescription = property("project.description").toString()
    val projectUrl = property("project.url").toString()
    val projectUrlScm = property("project.url.scm").toString()
    val projectLicense = property("project.license").toString()
    val projectLicenseUrl = property("project.license.url").toString()
    val projectDeveloperId = property("project.developer.id").toString()
    val projectDeveloperName = property("project.developer.name").toString()
    val projectDeveloperEmail = property("project.developer.email").toString()
    val projectDeveloperUrl = property("project.developer.url").toString()

    coordinates(
        groupId = projectGroup,
        artifactId = "$projectName-core",
        version = projectVersion
    )

    pom {
        name = projectName
        description = projectDescription
        inceptionYear = "${Year.now().value}"
        url = projectUrl
        licenses {
            license {
                name = projectLicense
                url = projectLicenseUrl
                distribution = projectUrl
            }
        }
        developers {
            developer {
                id = projectDeveloperId
                name = projectDeveloperName
                email = projectDeveloperEmail
                url = projectDeveloperUrl
            }
        }
        scm {
            url = projectUrlScm
            connection = "scm:git:git://github.com/${projectDeveloperId}"
            developerConnection = "scm:git:ssh://git@github.com/${projectDeveloperId}"
        }
    }
}