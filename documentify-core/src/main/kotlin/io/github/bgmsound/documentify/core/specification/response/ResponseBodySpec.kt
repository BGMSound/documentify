package io.github.bgmsound.documentify.core.specification.response

import io.github.bgmsound.documentify.core.specification.BodySpec
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Snippet

class ResponseBodySpec : BodySpec() {
    private var responseFieldsSnippet: ResponseFieldsSnippet? = null

    override fun build(): List<Snippet> {
        if (fields.isEmpty()) {
            return emptyList()
        }
        responseFieldsSnippet = PayloadDocumentation.responseFields(fields.map { it.descriptor })
        return listOf(responseFieldsSnippet!!)
    }
}