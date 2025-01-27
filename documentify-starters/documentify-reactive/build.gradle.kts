import java.time.Year
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    signing
    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.28.0"
}

dependencies {
    compileOnly(libs.spring.boot.starter.webflux)
    api(libs.spring.restdocs.webtestclient)
    api(libs.restdocs.api.spec)
    api(libs.restdocs.api.spec.webtestclient)
}

signing {
    useGpgCmd()
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
        artifactId = "$projectName-reactive",
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