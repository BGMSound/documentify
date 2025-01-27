package io.github.bgmsound.documentify.reactive.environment

import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.reactive.ReactiveDocumentContextEnvironment
import io.github.bgmsound.documentify.reactive.emitter.EmitterFactory
import io.github.bgmsound.documentify.reactive.emitter.ReactiveDocumentEmitter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient

class WebTestClientContextEnvironment private constructor(
    private val webTestClient: WebTestClient,
) : ReactiveDocumentContextEnvironment {
    override fun buildEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec
    ): ReactiveDocumentEmitter {
        return EmitterFactory.createReactiveEmitter(
            provider,
            documentSpec,
            webTestClient
                .mutate()
                .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
                .build()
        )
    }

    companion object {
        fun webTestClientEnvironment(webTestClient: WebTestClient): WebTestClientContextEnvironment {
            return WebTestClientContextEnvironment(webTestClient)
        }
    }
}