package io.github.bgmsound.documentify.core.specification.schema.document

import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec

class ValidatelessResponseCaseContext {
    val responses = mutableListOf<ResponseSpec>()

    fun response(specCustomizer: ResponseSpec.() -> Unit) {
        val response = ResponseSpec()
        response.apply(specCustomizer)
        responses.add(response)
    }
}