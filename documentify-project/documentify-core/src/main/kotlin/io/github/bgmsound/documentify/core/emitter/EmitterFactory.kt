package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.environment.DocumentContextEnvironment
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.springframework.restdocs.RestDocumentationContextProvider

object EmitterFactory {
    fun of(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec,
        environment: DocumentContextEnvironment
    ): DocumentEmitter {
        return WebTestClientDocumentEmitter(provider, documentSpec, environment)
    }
}