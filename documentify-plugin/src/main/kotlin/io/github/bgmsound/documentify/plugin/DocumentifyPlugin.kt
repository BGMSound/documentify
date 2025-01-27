package io.github.bgmsound.documentify.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class DocumentifyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (!target.plugins.hasPlugin("com.epages.restdocs-api-spec")) {
            target.pluginManager.apply("com.epages.restdocs-api-spec")
        }
    }
}