package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.emitter.DocumentEmitter
import io.github.bgmsound.documentify.core.emitter.EmitterFactory
import io.github.bgmsound.documentify.core.environment.ApplicationContextEnvironment.Companion.applicationContextEnvironment
import io.github.bgmsound.documentify.core.environment.DocumentContextEnvironment
import io.github.bgmsound.documentify.core.environment.WebTestClientContextEnvironment.Companion.webTestClientEnvironment
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.springframework.context.ApplicationContext
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.context.WebApplicationContext

abstract class AbstractDocumentifySupport : DocumentifySupport {
    protected lateinit var provider: RestDocumentationContextProvider
    protected lateinit var environment: DocumentContextEnvironment
    protected var customEmitter: DocumentEmitter? = null

    override fun documentation(
        name: String,
        printOption: PrintOption,
        specCustomizer: DocumentSpec.() -> Unit
    ): WebTestClient.BodyContentSpec {
        val documentSpec = DocumentSpec(name).also { specCustomizer(it) }
        val emitter = customEmitter ?: EmitterFactory.of(provider, documentSpec, environment, printOption)

        return emitter.emit()
    }

    override fun emitter(
        customEmitter: DocumentEmitter
    ) {
        this.customEmitter = customEmitter
    }

    override fun webTestClient(
        provider: RestDocumentationContextProvider,
        webTestClient: WebTestClient
    ) {
        this.provider = provider
        environment = webTestClientEnvironment(provider, webTestClient)
    }

    override fun applicationContext(
        provider: RestDocumentationContextProvider,
        applicationContext: ApplicationContext
    ) {
        this.provider = provider
        environment = applicationContextEnvironment(provider, applicationContext)
    }

    override fun webApplicationContext(
        provider: RestDocumentationContextProvider,
        context: WebApplicationContext
    ) {
        environment = applicationContextEnvironment(provider, context)
        this.provider = provider
    }
}