package io.github.bgmsound.documentify.core.specification.request

import io.github.bgmsound.documentify.core.specification.HeaderSpec
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.snippet.Snippet

class RequestHeaderSpec : HeaderSpec() {
    override fun build(): List<Snippet> {
        if (headers.isEmpty()) {
            return emptyList()
        }
        val headersSnippet = HeaderDocumentation.requestHeaders(headers.map { it.build() })
        return listOf(headersSnippet)
    }
}