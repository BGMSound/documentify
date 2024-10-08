package io.github.bgmsound.documentify.core.specification

import io.github.bgmsound.documentify.core.specification.request.RequestBodySpec
import io.github.bgmsound.documentify.core.specification.request.RequestHeaderSpec
import io.github.bgmsound.documentify.core.specification.request.RequestLineSpec
import io.github.bgmsound.documentify.core.specification.request.RequestSpec
import io.github.bgmsound.documentify.core.specification.response.ResponseBodySpec
import io.github.bgmsound.documentify.core.specification.response.ResponseHeaderSpec
import io.github.bgmsound.documentify.core.specification.response.ResponseSpec
import io.restassured.http.Method
import org.springframework.http.HttpStatus
import org.springframework.restdocs.snippet.Snippet

class DocumentSpec(
    val name: String
) : APISpec {
    val request: RequestSpec = RequestSpec()
    val response: ResponseSpec = ResponseSpec()
    private val information: InformationSpec = InformationSpec(name, request, response)

    fun information(specCustomizer: InformationSpec.() -> Unit) {
        information.apply(specCustomizer)
    }

    fun description(description: String) {
        information.description(description)
    }

    fun tag(tag: String) {
        information.tag(tag)
    }

    fun tags(vararg tags: String) {
        information.tags(*tags)
    }

    fun tags(tags: Collection<String>) {
        information.tags(tags)
    }

    fun request(specCustomizer: RequestSpec.() -> Unit) {
        request.apply(specCustomizer)
    }

    fun requestSchema(schema: String) {
        information.requestSchema(schema)
    }

    fun requestLine(
        method: Method,
        url: String,
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

    fun requestBody(schema: String, specCustomizer: RequestBodySpec.() -> Unit) {
        request.body(schema, specCustomizer)
    }

    fun response(specCustomizer: ResponseSpec.() -> Unit) {
        response.apply(specCustomizer)
    }

    fun responseSchema(schema: String) {
        information.responseSchema(schema)
    }

    fun responseStatus(
        status: HttpStatus
    ) = responseLine(status)

    fun responseStatus(
        status: Int
    ) = responseLine(status)

    fun responseLine(
        status: HttpStatus
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

    fun responseBody(schema: String, specCustomizer: ResponseBodySpec.() -> Unit) {
        response.body(schema, specCustomizer)
    }

    override fun build(): List<Snippet> {
        return buildList {
            addAll(request.build())
            addAll(response.build())
            addAll(information.build())
        }
    }
}