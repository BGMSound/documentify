import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.Year

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.gradle.mavenCentral.publish) apply false
    alias(libs.plugins.kotlinx.kover)
    java
    signing
    `maven-publish`
}

group = extra["project.group"] as String
version = extra["project.version.id"] as String

val publicModulePathSet = setOf(
    rootProject.projects.documentifyCore.identityPath.path,
    rootProject.projects.documentifyPlugin.identityPath.path,
    rootProject.projects.documentifyProject.documentifyMvc.identityPath.path,
    rootProject.projects.documentifyProject.documentifyReactive.identityPath.path,
    rootProject.projects.documentifyStarters.documentifyStarterMvc.identityPath.path,
    rootProject.projects.documentifyStarters.documentifyStarterReactive.identityPath.path
)

subprojects {
    group = rootProject.group
    version = rootProject.version
    with(pluginManager) {
        apply(rootProject.libs.plugins.kotlin.jvm.get().pluginId)
        apply(rootProject.libs.plugins.kotlin.plugin.spring.get().pluginId)
    }
    repositories {
        mavenCentral()
    }
    tasks {
        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        withType<KotlinCompile> {
            compilerOptions {
                freeCompilerArgs.add("-Xjsr305=strict")
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
        test {
            useJUnitPlatform()
        }
    }
    if (publicModulePathSet.contains(project.path)) {
        with(pluginManager) {
            apply(rootProject.libs.plugins.gradle.mavenCentral.publish.get().pluginId)
            apply(plugin = "signing")
            apply(plugin = "maven-publish")
        }
        configure<SigningExtension> {
            val gpgSecret = project.findProperty("gpg.secret").toString()
            val gpgPassphrase = project.findProperty("gpg.passphrase").toString()

            useInMemoryPgpKeys(gpgSecret, gpgPassphrase)
            sign(publishing.publications)
        }
        configure<MavenPublishBaseExtension> {
            publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

            val artifactId = name.replace("-plugin", ".gradle.plugin")
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
                artifactId = artifactId,
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
    }
}

repositories {
    mavenCentral()
}

kover {
    merge {
        projects(
            rootProject.projects.documentifyCore.identityPath.path,
            rootProject.projects.documentifyProject.documentifyMvc.identityPath.path,
            rootProject.projects.documentifyProject.documentifyReactive.identityPath.path,
            rootProject.projects.documentifySample.mvcSample.identityPath.path,
            rootProject.projects.documentifySample.reactiveSample.identityPath.path
        )
    }
    reports {
        filters {
            excludes.classes.add("**.sample.**")
        }
    }
}