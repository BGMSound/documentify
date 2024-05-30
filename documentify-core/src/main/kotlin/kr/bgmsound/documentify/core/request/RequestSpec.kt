package kr.bgmsound.documentify.core.request

import io.restassured.http.Method
import kr.bgmsound.documentify.core.APISpec
import kr.bgmsound.documentify.core.Field
import kr.bgmsound.documentify.core.Header
import kr.bgmsound.documentify.core.Parameter
import org.springframework.restdocs.snippet.Snippet

class RequestSpec(
    private val requestLine: RequestLineSpec = RequestLineSpec("", Method.GET),
    private val requestHeader: RequestHeaderSpec = RequestHeaderSpec(),
    private val requestBody: RequestBodySpec = RequestBodySpec()
) : APISpec {

    val url get() = requestLine.url
    val method get() = requestLine.method
    val pathVariables get() = requestLine.pathVariables().of()
    val queryParameters get() = requestLine.queryParameters().of()
    val headers get() = requestHeader.headers().of()
    val fields get() = requestBody.fields().of()

    fun line(
        method: Method,
        url: String
    ) = line(method, url) {}

    fun line(
        method: Method,
        url: String,
        specCustomizer: RequestLineSpec.() -> Unit
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

    override fun build(): List<Snippet> {
        return buildList {
            addAll(requestLine.build())
            addAll(requestHeader.build())
            addAll(requestBody.build())
        }
    }

    private fun List<Parameter>.of(): Map<String, String> {
        return associate { it.key to it.sample }
    }

    private fun List<Header>.of(): Map<String, Any> {
        return associate { it.key to it.sample }
    }

    private fun List<Field>.of(): Map<String, Any> {
        return associate { it.key to it.sample }
    }
}