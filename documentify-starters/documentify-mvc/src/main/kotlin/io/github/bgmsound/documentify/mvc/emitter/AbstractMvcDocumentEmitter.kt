package io.github.bgmsound.documentify.mvc.emitter

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentEmitter
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse
import org.springframework.restdocs.RestDocumentationContextProvider

abstract class AbstractMvcDocumentEmitter(
    provider: RestDocumentationContextProvider,
    documentSpec: DocumentSpec
) : AbstractDocumentEmitter(provider, documentSpec), MvcDocumentEmitter {
    override fun emit(): ValidatableMockMvcResponse {
        val validatableDocumentResult = emitDocument()
        emitAlternativeResponseDocument()

        return validatableDocumentResult
    }

    abstract fun emitDocument(): ValidatableMockMvcResponse

    abstract fun emitAlternativeResponseDocument()
}