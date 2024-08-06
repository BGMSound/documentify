plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "documentify"
include("documentify-core")
include("documentify-sample")
