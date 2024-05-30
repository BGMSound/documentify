package kr.bgmsound.documentify.core

import io.restassured.http.Method
import kr.bgmsound.documentify.core.request.RequestBodySpec
import kr.bgmsound.documentify.core.request.RequestHeaderSpec
import kr.bgmsound.documentify.core.request.RequestLineSpec
import kr.bgmsound.documentify.core.request.RequestSpec
import kr.bgmsound.documentify.core.response.ResponseBodySpec
import kr.bgmsound.documentify.core.response.ResponseHeaderSpec
import kr.bgmsound.documentify.core.response.ResponseSpec
import org.springframework.http.HttpStatus
import org.springframework.restdocs.snippet.Snippet

class DocumentSpec(
    val name: String,
) : APISpec {

    private val snippets = mutableListOf<Snippet>()

    val request: RequestSpec = RequestSpec()
    val response: ResponseSpec = ResponseSpec()

    fun request(specCustomizer: RequestSpec.() -> Unit) {
        request.apply(specCustomizer)
    }

    fun requestLine(
        method: Method,
        url: String
    ) = requestLine(method, url) {}

    fun requestLine(
        method: Method,
        url: String,
        specCustomizer: RequestLineSpec.() -> Unit
    ) {
        request.line(method, url, specCustomizer)
    }

    fun requestHeaders(specCustomizer: RequestHeaderSpec.() -> Unit) {
        request.headers(specCustomizer)
    }

    fun requestBody(specCustomizer: RequestBodySpec.() -> Unit) {
        request.body(specCustomizer)
    }

    fun response(specCustomizer: ResponseSpec.() -> Unit) {
        response.apply(specCustomizer)
    }

    fun responseLine(
        status: HttpStatus,
    ) = responseLine(status.value())

    fun responseLine(
        status: HttpStatus,
        specCustomizer: ResponseHeaderSpec.() -> Unit
    ) = responseLine(status.value(), specCustomizer)

    fun responseLine(
        status: Int
    ) = responseLine(status) {}

    fun responseLine(
        status: Int,
        specCustomizer: ResponseHeaderSpec.() -> Unit
    ) {
        response.status(status)
        response.headers(specCustomizer)
    }

    fun responseHeaders(
        specCustomizer: ResponseHeaderSpec.() -> Unit
    ) {
        response.headers(specCustomizer)
    }

    fun responseBody(specCustomizer: ResponseBodySpec.() -> Unit) {
        response.body(specCustomizer)
    }

    override fun build(): List<Snippet> {
         val subSnippets =  buildList {
            addAll(request.build())
            addAll(response.build())
        }
        snippets.addAll(subSnippets)
        return snippets
    }
}