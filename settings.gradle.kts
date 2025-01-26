rootProject.name = extra["project.name"] as String

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("documentify-core")
include("documentify-sample")
include("documentify-sample:reactive-sample")
include("documentify-sample:mvc-sample")
include("documentify-plugin")
include("documentify-starters")
include("documentify-starters:documentify-mvc")
include("documentify-starters:documentify-reactive")


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")