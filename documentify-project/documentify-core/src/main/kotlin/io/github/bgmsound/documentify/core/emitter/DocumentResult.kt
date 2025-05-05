package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec

interface DocumentResult {

    fun validateWith(responseSpec: ResponseSpec)

}