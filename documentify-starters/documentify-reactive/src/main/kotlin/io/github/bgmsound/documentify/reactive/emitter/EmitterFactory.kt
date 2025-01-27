package io.github.bgmsound.documentify.reactive.emitter

import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.test.web.reactive.server.WebTestClient

object EmitterFactory {
    fun createReactiveEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec,
        webTestClient: WebTestClient,
    ): ReactiveDocumentEmitter {
        return WebTestClientReactiveDocumentEmitter(provider, documentSpec, webTestClient)
    }
}