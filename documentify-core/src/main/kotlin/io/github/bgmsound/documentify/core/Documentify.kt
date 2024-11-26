package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.documentation.specification.document.DocumentSpec
import io.github.bgmsound.documentify.core.environment.StandaloneContext
import io.github.bgmsound.documentify.core.environment.StandaloneContext.Companion.controllers
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.method.support.HandlerMethodArgumentResolver

@ExtendWith(RestDocumentationExtension::class)
abstract class Documentify {
    private lateinit var provider: RestDocumentationContextProvider
    private lateinit var mockMvc: MockMvc

    fun documentation(
        name: String,
        specCustomizer: DocumentSpec.() -> Unit
    ) {
        val documentSpec = DocumentSpec(name).also { specCustomizer(it) }
        val emitter = EmitterFactory.emitterOf(documentSpec)
        emitter.emit(provider, mockMvc)
    }

    fun mockMvc(
        provider: RestDocumentationContextProvider,
        mockMvc: MockMvc
    ) {
        this.provider = provider
        this.mockMvc = mockMvc
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
        this.mockMvc = mockMvc
        this.provider = provider
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

    fun webApplicationContext(
        provider: RestDocumentationContextProvider,
        context: WebApplicationContext
    ) {
        val mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(provider))
            .build()
        this.mockMvc = mockMvc
        this.provider = provider
    }
}