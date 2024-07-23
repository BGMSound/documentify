package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.specification.DocumentSpec
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.HandlerExceptionResolver

@ExtendWith(RestDocumentationExtension::class)
abstract class Documentify {
    private lateinit var spec: MockMvcRequestSpecification

    fun standalone(
        restDocumentation: RestDocumentationContextProvider,
        controllers: List<Any>,
        controllerAdvices: List<Any>,
        argumentResolvers: List<HandlerMethodArgumentResolver>
    ) {
        val mockMvc = MockMvcBuilders
            .standaloneSetup(*controllers.toTypedArray())
            .setControllerAdvice(*controllerAdvices.toTypedArray())
            .setCustomArgumentResolvers(*argumentResolvers.toTypedArray())
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
        spec = given().mockMvc(mockMvc)
    }

    fun setup(mock: MockMvc) {
        spec = given().mockMvc(mock)
    }

    fun documentation(
        name: String,
        specCustomizer: DocumentSpec.() -> Unit
    ) {
        val documentSpec = DocumentSpec(name).also { specCustomizer(it) }
        val emitter = RestDocsEmitter(documentSpec)
        emitter.emit(spec)
    }
}