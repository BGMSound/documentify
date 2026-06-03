package io.github.bgmsound.documentify.reactive

import io.github.bgmsound.documentify.core.AbstractDocumentifySupport
import io.github.bgmsound.documentify.reactive.environment.StandaloneReactiveContextEnvironment
import io.github.bgmsound.documentify.reactive.environment.StandaloneReactiveContextEnvironment.Companion.standaloneEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider

class ReactiveDocumentify : AbstractDocumentifySupport(), Documentify {
    override fun standalone(
        provider: RestDocumentationContextProvider,
        standaloneContext: StandaloneReactiveContextEnvironment
    ) {
        this.provider = provider
        environment = standaloneContext
    }

    override fun standalone(
        provider: RestDocumentationContextProvider,
        contextCustomizer: StandaloneReactiveContextEnvironment.() -> Unit
    ) {
        val standaloneContext = standaloneEnvironment(provider).also(contextCustomizer)
        standalone(provider, standaloneContext)
    }
}