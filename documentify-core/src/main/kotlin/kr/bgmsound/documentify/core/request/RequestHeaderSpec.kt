package kr.bgmsound.documentify.core.request

import kr.bgmsound.documentify.core.HeaderSpec
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.RequestHeadersSnippet
import org.springframework.restdocs.snippet.Snippet

class RequestHeaderSpec : HeaderSpec() {
    private var _headersSnippet: RequestHeadersSnippet? = null

    override fun build(): List<Snippet> {
        if (headers.isEmpty()) {
            return emptyList()
        }
        _headersSnippet = HeaderDocumentation.requestHeaders(headers.map { it.descriptor })
        return listOf(_headersSnippet!!)
    }
}