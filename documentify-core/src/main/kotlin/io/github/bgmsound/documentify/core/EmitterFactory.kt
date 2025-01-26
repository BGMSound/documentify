package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.documentation.specification.document.DocumentSpec
import io.github.bgmsound.documentify.core.emitter.DocumentEmitter
import io.github.bgmsound.documentify.core.emitter.RestAssuredDocumentEmitter

object EmitterFactory {
    fun emitterOf(documentSpec: DocumentSpec): DocumentEmitter {
        return RestAssuredDocumentEmitter(documentSpec)
    }
}