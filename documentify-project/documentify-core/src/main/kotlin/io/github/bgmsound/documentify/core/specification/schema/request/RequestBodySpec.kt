package io.github.bgmsound.documentify.core.specification.schema.request

import io.github.bgmsound.documentify.core.specification.schema.BodySpec
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Snippet

class RequestBodySpec : BodySpec() {
    override fun build(): List<Snippet> {
        if (fields.isEmpty()) {
            return emptyList()
        }
        val fieldsSnippet = PayloadDocumentation.requestFields(buildList { fields.forEach { addAll(it.build()) } })
        return listOf(fieldsSnippet)
    }
}