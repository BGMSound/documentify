package io.github.bgmsound.documentify.reactive

import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.reactive.emitter.EmitterFactory
import io.github.bgmsound.documentify.reactive.emitter.ReactiveDocumentEmitter
import io.github.bgmsound.documentify.reactive.environment.ApplicationContextEnvironment.Companion.applicationContextEnvironment
import io.github.bgmsound.documentify.reactive.environment.StandaloneReactiveContextEnvironment
import io.github.bgmsound.documentify.reactive.environment.StandaloneReactiveContextEnvironment.Companion.standaloneEnvironment
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
    private lateinit var environment: ReactiveDocumentContextEnvironment
    private var customEmitter: ReactiveDocumentEmitter? = null

    suspend fun documentation(
        name: String,
        specCustomizer: DocumentSpec.() -> Unit
    ): WebTestClient.BodyContentSpec {
        val documentSpec = DocumentSpec(name).also { specCustomizer(it) }
        val emitter = customEmitter ?: EmitterFactory.of(provider, documentSpec, environment)

        return emitter.emit()
    }

    fun emitter(
        customEmitter: ReactiveDocumentEmitter
    ) {
        this.customEmitter = customEmitter
    }

    fun webTestClient(
        provider: RestDocumentationContextProvider,
        webTestClient: WebTestClient
    ) {
        this.provider = provider
        environment = webTestClientEnvironment(provider, webTestClient)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        standaloneContext: StandaloneReactiveContextEnvironment
    ) {
        this.provider = provider
        environment = standaloneContext
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        contextCustomizer: StandaloneReactiveContextEnvironment.() -> Unit
    ) {
        val standaloneContext = standaloneEnvironment(provider).also(contextCustomizer)
        standalone(provider, standaloneContext)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        controllers: List<Any>,
        controllerAdvices: List<Any>,
        argumentResolvers: List<HandlerMethodArgumentResolver>
    ) {
        val standaloneContext = standaloneEnvironment(provider)
            .controllers(controllers)
            .controllerAdvices(controllerAdvices)
            .argumentResolvers(argumentResolvers)
        standalone(provider, standaloneContext)
    }

    fun applicationContext(
        provider: RestDocumentationContextProvider,
        applicationContext: ApplicationContext
    ) {
        this.provider = provider
        environment = applicationContextEnvironment(provider, applicationContext)
    }
}