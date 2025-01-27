import java.time.Year
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish") version "0.28.0"
    `java-gradle-plugin`
    `maven-publish`
    signing
}

repositories {
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        register("documentify") {
            id = "io.github.bgmsound.documentify"
            implementationClass = "io.github.bgmsound.documentify.plugin.DocumentifyPlugin"
            displayName = "Documentify"
            description = "easy and powerful API documentation tool for spring restdocs"
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation(libs.restdocs.api.spec.gradlePlugin)
}


signing {
    val gpgSecret = System.getenv("GPG_SECRET")
    val gpgPassphrase = System.getenv("GPG_PASSPHRASE")

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
        artifactId = "$projectName.gradle.plugin",
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