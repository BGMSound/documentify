package io.github.bgmsound.documentify.core.specification.request

import io.github.bgmsound.documentify.core.specification.BodySpec
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.snippet.Snippet

class RequestBodySpec : BodySpec() {
    private var fieldsSnippet: RequestFieldsSnippet? = null

    override fun build(): List<Snippet> {
        if (fields.isEmpty()) {
            return emptyList()
        }
        fieldsSnippet = PayloadDocumentation.requestFields(fields.map { it.descriptor })
        return listOf(fieldsSnippet!!)
    }
}