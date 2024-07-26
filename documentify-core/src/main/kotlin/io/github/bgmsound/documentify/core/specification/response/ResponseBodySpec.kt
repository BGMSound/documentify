package io.github.bgmsound.documentify.core.specification.response

import io.github.bgmsound.documentify.core.specification.BodySpec
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Snippet

class ResponseBodySpec : BodySpec() {
    override fun build(): List<Snippet> {
        if (fields.isEmpty()) {
            return emptyList()
        }
        val fieldsSnippet = PayloadDocumentation.responseFields(buildList { fields.forEach { addAll(it.build()) } })
        return listOf(fieldsSnippet)
    }
}