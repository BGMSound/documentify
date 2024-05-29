package kr.bgmsound.documentify.core.response

import kr.bgmsound.documentify.core.Spec
import org.springframework.http.HttpStatus
import org.springframework.restdocs.snippet.Snippet

class ResponseSpec(
    private val responseLineSpec: ResponseLineSpec = ResponseLineSpec(HttpStatus.OK),
    private val responseHeaderSpec: ResponseHeaderSpec = ResponseHeaderSpec(),
    private val responseBodySpec: ResponseBodySpec = ResponseBodySpec(),
) : Spec {

    val statusCode get() = responseLineSpec.statusCode()

    fun status(statusCode: HttpStatus) {
        responseLineSpec.statusCode(statusCode.value())
    }

    fun status(statusCode: Int) {
        responseLineSpec.statusCode(statusCode)
    }

    fun line(specCustomizer: ResponseLineSpec.() -> Unit) {
        responseLineSpec.apply(specCustomizer)
    }

    fun headers(spec: ResponseHeaderSpec.() -> Unit) {
        responseHeaderSpec.apply(spec)
    }

    fun body(spec: ResponseBodySpec.() -> Unit) {
        responseBodySpec.apply(spec)
    }

    override fun build(): List<Snippet> {
        return buildList {
            addAll(responseHeaderSpec.build())
            addAll(responseBodySpec.build())
        }
    }
}