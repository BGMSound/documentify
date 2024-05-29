package kr.bgmsound.documentify.core.request

import io.restassured.http.Method
import kr.bgmsound.documentify.core.APISpec
import org.springframework.restdocs.snippet.Snippet

class RequestSpec(
    private val requestLineSpec: RequestLineSpec = RequestLineSpec("", Method.GET),
    private val requestHeaderSpec: RequestHeaderSpec = RequestHeaderSpec(),
    private val requestBodySpec: RequestBodySpec = RequestBodySpec()
) : APISpec {

    val url get() = requestLineSpec.url
    val method get() = requestLineSpec.method

    fun line(
        method: Method,
        url: String
    ) = line(method, url) {}

    fun line(
        method: Method,
        url: String,
        specCustomizer: RequestLineSpec.() -> Unit
    ) {
        requestLineSpec.url = url
        requestLineSpec.method = method
        requestLineSpec.apply(specCustomizer)
    }

    fun headers(specCustomizer: RequestHeaderSpec.() -> Unit) {
        requestHeaderSpec.apply(specCustomizer)
    }

    fun body(specCustomizer: RequestBodySpec.() -> Unit) {
        requestBodySpec.apply(specCustomizer)
    }

    override fun build(): List<Snippet> {
        return buildList {
            addAll(requestLineSpec.build())
            addAll(requestHeaderSpec.build())
            addAll(requestBodySpec.build())
        }
    }
}