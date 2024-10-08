package io.github.bgmsound.documentify.core.specification.request

import io.github.bgmsound.documentify.core.specification.HttpSpec
import io.restassured.http.Method
import org.springframework.restdocs.snippet.Snippet

class RequestSpec(
    private val requestLine: RequestLineSpec = RequestLineSpec("", Method.GET),
    private val requestHeader: RequestHeaderSpec = RequestHeaderSpec(),
    private val requestBody: RequestBodySpec = RequestBodySpec(),
) : HttpSpec(requestHeader, requestBody) {

    val url get() = requestLine.url
    val method get() = requestLine.method
    val pathVariables get() = requestLine.pathVariables()
    val queryParameters get() = requestLine.queryParameters()

    fun line(
        method: Method,
        url: String,
    ) = line(method, url) {}

    fun line(
        method: Method,
        url: String,
        specCustomizer: RequestLineSpec.() -> Unit,
    ) {
        requestLine.url = url
        requestLine.method = method
        requestLine.apply(specCustomizer)
    }

    fun headers(specCustomizer: RequestHeaderSpec.() -> Unit) {
        requestHeader.apply(specCustomizer)
    }

    fun body(specCustomizer: RequestBodySpec.() -> Unit) {
        requestBody.apply(specCustomizer)
    }

    fun body(schema: String, specCustomizer: RequestBodySpec.() -> Unit) {
        requestBody.schema(schema)
        requestBody.apply(specCustomizer)
    }

    override fun build(): List<Snippet> {
        return buildList {
            addAll(requestLine.build())
            addAll(requestHeader.build())
            addAll(requestBody.build())
        }
    }
}