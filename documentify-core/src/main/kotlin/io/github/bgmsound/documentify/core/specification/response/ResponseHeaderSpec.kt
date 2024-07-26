package io.github.bgmsound.documentify.core.specification.response

import io.github.bgmsound.documentify.core.specification.HeaderSpec
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.snippet.Snippet

class ResponseHeaderSpec : HeaderSpec() {
    override fun build(): List<Snippet> {
        if (headers.isEmpty()) {
            return emptyList()
        }
        val headersSnippet = HeaderDocumentation.responseHeaders(headers.map { it.build() })
        return listOf(headersSnippet)
    }
}