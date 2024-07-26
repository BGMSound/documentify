package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.settings.StandaloneSettings
import io.github.bgmsound.documentify.core.specification.DocumentSpec
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import io.github.bgmsound.documentify.core.settings.StandaloneSettings.Companion.controllers
import io.github.bgmsound.documentify.core.settings.StandaloneSettings.Companion.settings

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.method.support.HandlerMethodArgumentResolver

@ExtendWith(RestDocumentationExtension::class)
abstract class Documentify {
    private lateinit var spec: MockMvcRequestSpecification

    fun documentation(
        name: String,
        specCustomizer: DocumentSpec.() -> Unit
    ) {
        val documentSpec = DocumentSpec(name).also { specCustomizer(it) }
        val emitter = RestDocsEmitter(documentSpec)
        emitter.emit(spec)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        controllers: List<Any>,
        controllerAdvices: List<Any>,
        argumentResolvers: List<HandlerMethodArgumentResolver>
    ) {
        val mockMvc = controllers(controllers)
            .controllerAdvices(controllerAdvices)
            .argumentResolvers(argumentResolvers)
            .build(provider)
        spec = given().mockMvc(mockMvc)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        settingCustomizer: StandaloneSettings.() -> Unit
    ) {
        val settings = settings().also(settingCustomizer)
        val mockMvc = settings.build(provider)
        spec = given().mockMvc(mockMvc)
    }

    fun setup(mock: MockMvc) {
        spec = given().mockMvc(mock)
    }
}