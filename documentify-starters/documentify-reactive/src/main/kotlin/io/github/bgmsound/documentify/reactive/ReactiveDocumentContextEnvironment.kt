package io.github.bgmsound.documentify.reactive

import io.github.bgmsound.documentify.core.environment.DocumentContextEnvironment
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.reactive.emitter.ReactiveDocumentEmitter
import org.springframework.restdocs.RestDocumentationContextProvider

interface ReactiveDocumentContextEnvironment : DocumentContextEnvironment {

    fun buildEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec,
    ): ReactiveDocumentEmitter

}