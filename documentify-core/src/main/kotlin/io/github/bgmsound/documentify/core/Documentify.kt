package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.environment.StandaloneContext
import io.github.bgmsound.documentify.core.specification.DocumentSpec
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import io.github.bgmsound.documentify.core.environment.StandaloneContext.Companion.controllers

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

    fun with(mock: MockMvc) {
        spec = given().mockMvc(mock)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        contextCustomizer: StandaloneContext.() -> Unit
    ) {
        val standaloneContext = controllers().also(contextCustomizer)
        standalone(provider, standaloneContext)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        standaloneContext: StandaloneContext
    ) {
        val mockMvc = standaloneContext.build(provider)
        spec = given().mockMvc(mockMvc)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        controllers: List<Any>,
        controllerAdvices: List<Any>,
        argumentResolvers: List<HandlerMethodArgumentResolver>
    ) {
        val standaloneContext = controllers(controllers)
            .controllerAdvices(controllerAdvices)
            .argumentResolvers(argumentResolvers)
        standalone(provider, standaloneContext)
    }
}