rootProject.name = "documentify"

include("documentify-core")
include("documentify-example")

pluginManagement {
    fun property(key: String): String {
        return extra[key]?.toString() ?: throw IllegalArgumentException("property with $key not found")
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version embeddedKotlinVersion
        kotlin("plugin.spring") version embeddedKotlinVersion

        id("org.springframework.boot") version property("spring-boot-version")
        id("io.spring.dependency-management") version "1.1.4"
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("spring") {
            library("test", "org.springframework.boot:spring-boot-starter-test:${property("spring-boot-version")}")
            library("restdocs-mockmvc", "org.springframework.restdocs:spring-restdocs-mockmvc:${property("spring-restdocs-version")}")
            library("restdocs-assured", "org.springframework.restdocs:spring-restdocs-restassured:${property("spring-restdocs-version")}")
        }
        create("lib") {
            library("rest-assured", "io.rest-assured:spring-mock-mvc:5.3.1")
        }
    }
}

fun property(key: String): String {
    return extra[key]?.toString() ?: throw IllegalArgumentException("property with $key not found")
}