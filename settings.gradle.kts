rootProject.name = extra["project.name"] as String

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("documentify-core")
include("documentify-plugin")
include("documentify-project")
include("documentify-project:documentify-mvc")
include("documentify-project:documentify-reactive")
include("documentify-sample")
include("documentify-sample:reactive-sample")
include("documentify-sample:mvc-sample")
include("documentify-starters")
include("documentify-starters:documentify-starter-mvc")
include("documentify-starters:documentify-starter-reactive")


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")