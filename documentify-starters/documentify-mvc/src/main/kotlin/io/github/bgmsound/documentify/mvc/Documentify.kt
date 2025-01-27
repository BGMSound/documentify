package io.github.bgmsound.documentify.mvc

import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.mvc.environment.MockMvcContextEnvironment.Companion.mockMvcEnvironment
import io.github.bgmsound.documentify.mvc.environment.StandaloneMvcContextEnvironment
import io.github.bgmsound.documentify.mvc.environment.WebApplicationContextEnvironment.Companion.webApplicationContextEnvironment
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.method.support.HandlerMethodArgumentResolver

@ExtendWith(RestDocumentationExtension::class)
abstract class Documentify {
    private lateinit var provider: RestDocumentationContextProvider
    private lateinit var documentContextEnvironment: MvcDocumentContextEnvironment

    fun documentation(
        name: String,
        specCustomizer: DocumentSpec.() -> Unit
    ) {
        val documentSpec = DocumentSpec(name).also { specCustomizer(it) }
        val emitter = documentContextEnvironment.buildEmitter(provider, documentSpec)

        emitter.emit()
    }

    fun mockMvc(
        provider: RestDocumentationContextProvider,
        mockMvc: MockMvc
    ) {
        this.provider = provider
        documentContextEnvironment = mockMvcEnvironment(mockMvc)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        standaloneContext: StandaloneMvcContextEnvironment
    ) {
        this.provider = provider
        documentContextEnvironment = standaloneContext
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        contextCustomizer: StandaloneMvcContextEnvironment.() -> Unit
    ) {
        val standaloneContext = StandaloneMvcContextEnvironment.controllers().also(contextCustomizer)
        standalone(provider, standaloneContext)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        controllers: List<Any>,
        controllerAdvices: List<Any>,
        argumentResolvers: List<HandlerMethodArgumentResolver>
    ) {
        val standaloneContext = StandaloneMvcContextEnvironment.controllers(controllers)
            .controllerAdvices(controllerAdvices)
            .argumentResolvers(argumentResolvers)
        standalone(provider, standaloneContext)
    }

    fun webApplicationContext(
        provider: RestDocumentationContextProvider,
        context: WebApplicationContext
    ) {
        documentContextEnvironment = webApplicationContextEnvironment(context)
        this.provider = provider
    }
}