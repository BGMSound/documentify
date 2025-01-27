package io.github.bgmsound.documentify.reactive.emitter

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentEmitter
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.springframework.restdocs.RestDocumentationContextProvider

abstract class AbstractReactiveDocumentEmitter(
    provider: RestDocumentationContextProvider,
    documentSpec: DocumentSpec,
) : AbstractDocumentEmitter(provider, documentSpec), ReactiveDocumentEmitter {

    override suspend fun emit() {
        emitDocument()
        emitAlternativeResponseDocument()
    }

    abstract suspend fun emitDocument()

    abstract suspend fun emitAlternativeResponseDocument()

}