package io.github.bgmsound.documentify.mvc.emitter

import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.mvc.MvcDocumentContextEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider

object EmitterFactory {
    fun createMvcEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec,
        environment: MvcDocumentContextEnvironment
    ): MvcDocumentEmitter {
        return RestAssuredMvcDocumentEmitter(provider, documentSpec, environment)
    }
}