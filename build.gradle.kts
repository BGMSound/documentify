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
    rootProject.projects.documentifyProject.documentifyCore.identityPath.path,
    rootProject.projects.documentifyProject.documentifyGradlePlugin.identityPath.path,
    rootProject.projects.documentifyProject.documentifyMvc.identityPath.path,
    rootProject.projects.documentifyProject.documentifyReactive.identityPath.path,
    rootProject.projects.documentifyStarters.documentifyStarterMvc.identityPath.path,
    rootProject.projects.documentifyStarters.documentifyStarterReactive.identityPath.path
)

repositories {
    mavenCentral()
}

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
        tasks.withType<Jar> {
            archiveClassifier.set("")
        }
        tasks.withType(PublishToMavenRepository::class.java) {
            dependsOn(tasks.withType<Sign>())
        }
        extensions.getByType<JavaPluginExtension>().apply {
            withSourcesJar()
            withJavadocJar()
        }
        publishing {
            repositories {
                maven {
                    name = "sonatype"
                    url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = System.getenv("MAVEN_CENTRAL_USERNAME")
                        password = System.getenv("MAVEN_CENTRAL_PASSWORD")
                    }
                }
            }
            publications {
                create<MavenPublication>("mavenCentral") {
                    from(components["java"])
                    groupId = property("project.group").toString()
                    artifactId = name.replace("-gradle-plugin", ".gradle.plugin")
                    version = property("project.version.id").toString()

                    pom {
                        val projectName = property("project.name").toString()
                        val projectDescription = property("project.description").toString()
                        val projectUrl = property("project.url").toString()
                        val projectUrlScm = property("project.url.scm").toString()
                        val projectLicense = property("project.license").toString()
                        val projectLicenseUrl = property("project.license.url").toString()
                        val projectDeveloperId = property("project.developer.id").toString()
                        val projectDeveloperName = property("project.developer.name").toString()
                        val projectDeveloperEmail = property("project.developer.email").toString()
                        val projectDeveloperUrl = property("project.developer.url").toString()

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
                    signing.sign(this@publications)
                }
            }
        }
    }
}

kover {
    merge {
        projects(
            rootProject.projects.documentifyProject.documentifyCore.identityPath.path,
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
