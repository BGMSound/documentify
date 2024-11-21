package io.github.bgmsound.documentify.core.documentation.specification.request

import io.github.bgmsound.documentify.core.documentation.element.SpecElement.Requirement
import io.github.bgmsound.documentify.core.documentation.element.header.Header
import io.github.bgmsound.documentify.core.documentation.specification.HeaderSpec
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.snippet.Snippet

class RequestHeaderSpec : HeaderSpec() {
    fun bearer(path: String, description: String, sample: String): Header {
        val header = Header.newHeader(
            path,
            description,
            "Bearer $sample",
            Requirement.REQUIRED
        )
        return header(header)
    }

    fun bearer(description: String, sample: String): Header {
        val header = Header.newHeader(
            "Authorization",
            description,
            "Bearer $sample",
            Requirement.REQUIRED
        )
        return header(header)
    }

    override fun build(): List<Snippet> {
        if (headers.isEmpty()) {
            return emptyList()
        }
        val headersSnippet = HeaderDocumentation.requestHeaders(headers.map { it.build() })
        return listOf(headersSnippet)
    }
}