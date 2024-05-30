package kr.bgmsound.documentify.core.response

import kr.bgmsound.documentify.core.HeaderSpec
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.ResponseHeadersSnippet
import org.springframework.restdocs.snippet.Snippet

class ResponseHeaderSpec : HeaderSpec() {
    private var headersSnippet: ResponseHeadersSnippet? = null

    override fun build(): List<Snippet> {
        if (headers.isEmpty()) {
            return emptyList()
        }
        headersSnippet = HeaderDocumentation.responseHeaders(headers.map { it.descriptor })
        return listOf(headersSnippet!!)
    }
}