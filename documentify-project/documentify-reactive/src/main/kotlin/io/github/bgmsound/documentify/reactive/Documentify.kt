package io.github.bgmsound.documentify.reactive

import io.github.bgmsound.documentify.core.DocumentifySupport
import io.github.bgmsound.documentify.reactive.environment.StandaloneReactiveContextEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider

interface Documentify : DocumentifySupport {

    fun standalone(provider: RestDocumentationContextProvider, standaloneContext: StandaloneReactiveContextEnvironment)

    fun standalone(provider: RestDocumentationContextProvider, contextCustomizer: StandaloneReactiveContextEnvironment.() -> Unit)

    companion object {

        fun new(): Documentify = ReactiveDocumentify()
    }

}