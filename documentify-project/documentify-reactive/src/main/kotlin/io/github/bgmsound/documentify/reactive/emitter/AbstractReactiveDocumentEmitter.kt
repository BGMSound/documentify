package io.github.bgmsound.documentify.reactive.emitter

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentEmitter
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.reactive.emitter.ReactiveDocumentResult.Companion.validateWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec

abstract class AbstractReactiveDocumentEmitter(
    provider: RestDocumentationContextProvider,
    documentSpec: DocumentSpec,
) : AbstractDocumentEmitter(provider, documentSpec), ReactiveDocumentEmitter {

    override suspend fun emit(): BodyContentSpec {
        val validatableDocumentResponse = emitDocument()
        emitAlternativeResponseDocument()
        validatableDocumentResponse.validateWith(documentSpec.response)

        return validatableDocumentResponse
    }

    abstract suspend fun emitDocument(): BodyContentSpec

    abstract suspend fun emitAlternativeResponseDocument()

}