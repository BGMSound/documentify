package io.github.bgmsound.documentify.mvc.emitter

import io.github.bgmsound.documentify.mvc.ValidatableMockResponse

interface MvcDocumentEmitter {

    fun emit(): ValidatableMockResponse

}