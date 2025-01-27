package io.github.bgmsound.documentify.mvc.emitter

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentEmitter
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.springframework.restdocs.RestDocumentationContextProvider

abstract class AbstractMvcDocumentEmitter(
    provider: RestDocumentationContextProvider,
    documentSpec: DocumentSpec
) : AbstractDocumentEmitter(provider, documentSpec), MvcDocumentEmitter {
    override fun emit() {
        emitDocument()
        emitAlternativeResponseDocument()
    }

    abstract fun emitDocument()

    abstract fun emitAlternativeResponseDocument()
}