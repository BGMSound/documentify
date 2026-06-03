package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.emitter.DocumentEmitter
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationContext
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.context.WebApplicationContext

@ExtendWith(RestDocumentationExtension::class)
interface DocumentifySupport {

    fun documentation(name: String, specCustomizer: DocumentSpec.() -> Unit): WebTestClient.BodyContentSpec = documentation(name, PrintOption.ON, specCustomizer)

    fun documentation(
        name: String,
        printOption: PrintOption,
        specCustomizer: DocumentSpec.() -> Unit
    ): WebTestClient.BodyContentSpec

    fun emitter(customEmitter: DocumentEmitter)

    fun webTestClient(provider: RestDocumentationContextProvider, webTestClient: WebTestClient)

    fun applicationContext(provider: RestDocumentationContextProvider, applicationContext: ApplicationContext)

    fun webApplicationContext(provider: RestDocumentationContextProvider, context: WebApplicationContext)

}