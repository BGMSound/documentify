package io.github.bgmsound.documentify.reactive.environment

import io.github.bgmsound.documentify.reactive.AbstractReactiveDocumentContextEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient

class WebTestClientContextEnvironment private constructor(
    private val provider: RestDocumentationContextProvider,
    private val webTestClient: WebTestClient
) : AbstractReactiveDocumentContextEnvironment() {
    override fun buildWebTestClient(): WebTestClient {
        return webTestClient
                .mutate()
                .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
                .build()
    }

    companion object {
        fun webTestClientEnvironment(
            provider: RestDocumentationContextProvider,
            webTestClient: WebTestClient
        ): WebTestClientContextEnvironment {
            return WebTestClientContextEnvironment(provider, webTestClient)
        }
    }
}