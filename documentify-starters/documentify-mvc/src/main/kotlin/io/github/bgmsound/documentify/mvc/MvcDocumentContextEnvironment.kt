package io.github.bgmsound.documentify.mvc

import io.github.bgmsound.documentify.core.environment.DocumentContextEnvironment
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.mvc.emitter.MvcDocumentEmitter
import org.springframework.restdocs.RestDocumentationContextProvider

interface MvcDocumentContextEnvironment : DocumentContextEnvironment {

    fun buildEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec
    ): MvcDocumentEmitter

}