package io.github.bgmsound.documentify.mvc.emitter

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentEmitter
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.mvc.ValidatableMockMvcResponseAdapter
import io.github.bgmsound.documentify.mvc.ValidatableMockResponse
import io.github.bgmsound.documentify.mvc.emitter.MvcDocumentResult.Companion.validateWith
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse
import org.springframework.restdocs.RestDocumentationContextProvider

abstract class AbstractMvcDocumentEmitter(
    provider: RestDocumentationContextProvider,
    documentSpec: DocumentSpec
) : AbstractDocumentEmitter(provider, documentSpec), MvcDocumentEmitter {
    override fun emit(): ValidatableMockResponse {
        val documentResult = ValidatableMockMvcResponseAdapter.of(emitDocument())
        documentResult.validateWith(documentSpec.response)
        emitAlternativeResponseDocument()

        return documentResult
    }

    abstract fun emitDocument(): ValidatableMockMvcResponse

    abstract fun emitAlternativeResponseDocument()
}