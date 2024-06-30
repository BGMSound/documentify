package kr.bgmsound.documentify.core.specification.request

import kr.bgmsound.documentify.core.specification.HeaderSpec
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.RequestHeadersSnippet
import org.springframework.restdocs.snippet.Snippet

class RequestHeaderSpec : HeaderSpec() {
    private var headersSnippet: RequestHeadersSnippet? = null

    override fun build(): List<Snippet> {
        if (headers.isEmpty()) {
            return emptyList()
        }
        headersSnippet = HeaderDocumentation.requestHeaders(headers.map { it.descriptor })
        return listOf(headersSnippet!!)
    }
}