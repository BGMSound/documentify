package io.github.bgmsound.documentify.reactive.environment

import io.github.bgmsound.documentify.reactive.ReactiveDocumentContextEnvironment
import org.springframework.context.ApplicationContext
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient

class ApplicationContextEnvironment private constructor(
    private val provider: RestDocumentationContextProvider,
    private val applicationContext: ApplicationContext
) : ReactiveDocumentContextEnvironment {

    companion object {
        fun applicationContextEnvironment(
            provider: RestDocumentationContextProvider,
            context: ApplicationContext
        ): ApplicationContextEnvironment {
            return ApplicationContextEnvironment(provider, context)
        }
    }

    override fun buildWebTestClient(): WebTestClient {
        return WebTestClient
            .bindToApplicationContext(applicationContext)
            .configureClient()
            .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
            .build()
    }
}