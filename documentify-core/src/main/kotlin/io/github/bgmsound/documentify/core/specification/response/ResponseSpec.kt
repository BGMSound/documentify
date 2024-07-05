package io.github.bgmsound.documentify.core.specification.response

import io.github.bgmsound.documentify.core.specification.APISpec
import org.springframework.http.HttpStatus
import org.springframework.restdocs.snippet.Snippet

class ResponseSpec(
    private val responseLine: ResponseLineSpec = ResponseLineSpec(HttpStatus.OK),
    private val responseHeader: ResponseHeaderSpec = ResponseHeaderSpec(),
    private val responseBody: ResponseBodySpec = ResponseBodySpec(),
) : APISpec {

    val statusCode get() = responseLine.statusCode()

    val headers get() = responseHeader.headers()

    val fields get() = responseBody.fields()

    fun status(statusCode: HttpStatus) {
        responseLine.statusCode(statusCode.value())
    }

    fun status(statusCode: Int) {
        responseLine.statusCode(statusCode)
    }

    fun line(specCustomizer: ResponseLineSpec.() -> Unit) {
        responseLine.apply(specCustomizer)
    }

    fun headers(spec: ResponseHeaderSpec.() -> Unit) {
        responseHeader.apply(spec)
    }

    fun body(spec: ResponseBodySpec.() -> Unit) {
        responseBody.apply(spec)
    }

    override fun build(): List<Snippet> {
        return buildList {
            addAll(responseHeader.build())
            addAll(responseBody.build())
        }
    }
}