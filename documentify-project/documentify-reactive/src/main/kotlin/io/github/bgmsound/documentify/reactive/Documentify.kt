package io.github.bgmsound.documentify.reactive

import io.github.bgmsound.documentify.core.AbstractDocumentify
import io.github.bgmsound.documentify.reactive.environment.StandaloneReactiveContextEnvironment
import io.github.bgmsound.documentify.reactive.environment.StandaloneReactiveContextEnvironment.Companion.standaloneEnvironment
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension

@ExtendWith(RestDocumentationExtension::class)
abstract class Documentify : AbstractDocumentify() {
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
}