package io.github.bgmsound.documentify.reactive.environment

import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.reactive.ReactiveDocumentContextEnvironment
import io.github.bgmsound.documentify.reactive.emitter.EmitterFactory
import io.github.bgmsound.documentify.reactive.emitter.ReactiveDocumentEmitter
import org.springframework.context.ApplicationContext
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient

class ApplicationContextEnvironment private constructor(
    private val applicationContext: ApplicationContext
) : ReactiveDocumentContextEnvironment {

    companion object {
        fun applicationContextEnvironment(context: ApplicationContext): ApplicationContextEnvironment {
            return ApplicationContextEnvironment(context)
        }
    }

    override fun buildEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec,
    ): ReactiveDocumentEmitter {
        val webTestClient = WebTestClient
            .bindToApplicationContext(applicationContext)
            .configureClient()
            .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
            .build()
        return EmitterFactory.createReactiveEmitter(provider, documentSpec, webTestClient)
    }
}