package io.github.bgmsound.documentify.reactive

import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.reactive.environment.ApplicationContextEnvironment.Companion.applicationContextEnvironment
import io.github.bgmsound.documentify.reactive.environment.StandaloneReactiveContextEnvironment
import io.github.bgmsound.documentify.reactive.environment.StandaloneReactiveContextEnvironment.Companion.controllers
import io.github.bgmsound.documentify.reactive.environment.WebTestClientContextEnvironment.Companion.webTestClientEnvironment
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationContext
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver

@ExtendWith(RestDocumentationExtension::class)
abstract class Documentify {
    private lateinit var provider: RestDocumentationContextProvider
    private lateinit var documentContextEnvironment: ReactiveDocumentContextEnvironment

    suspend fun documentation(
        name: String,
        specCustomizer: DocumentSpec.() -> Unit
    ) {
        val documentSpec = DocumentSpec(name).also { specCustomizer(it) }
        val emitter = documentContextEnvironment.buildEmitter(provider, documentSpec)

        emitter.emit()
    }

    fun webTestClient(
        provider: RestDocumentationContextProvider,
        webTestClient: WebTestClient
    ) {
        this.provider = provider
        documentContextEnvironment = webTestClientEnvironment(webTestClient)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        standaloneContext: StandaloneReactiveContextEnvironment
    ) {
        this.provider = provider
        documentContextEnvironment = standaloneContext
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        contextCustomizer: StandaloneReactiveContextEnvironment.() -> Unit
    ) {
        val standaloneContext = controllers().also(contextCustomizer)
        standalone(provider, standaloneContext)
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

    fun applicationContext(
        provider: RestDocumentationContextProvider,
        applicationContext: ApplicationContext
    ) {
        this.provider = provider
        documentContextEnvironment = applicationContextEnvironment(applicationContext)
    }
}