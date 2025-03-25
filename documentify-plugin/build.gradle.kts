plugins {
    `java-gradle-plugin`
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