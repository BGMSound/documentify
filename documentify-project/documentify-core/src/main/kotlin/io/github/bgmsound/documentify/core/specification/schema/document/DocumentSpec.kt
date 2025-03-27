package io.github.bgmsound.documentify.core.specification.schema.document

import io.github.bgmsound.documentify.core.specification.DocumentableSpec
import io.github.bgmsound.documentify.core.specification.ResourceSpec
import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.core.specification.schema.request.RequestBodySpec
import io.github.bgmsound.documentify.core.specification.schema.request.RequestHeaderSpec
import io.github.bgmsound.documentify.core.specification.schema.request.RequestLineSpec
import io.github.bgmsound.documentify.core.specification.schema.request.RequestSpec
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseBodySpec
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseHeaderSpec
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.springframework.http.HttpStatus
import org.springframework.restdocs.snippet.Snippet

class DocumentSpec(
    val name: String
) : DocumentableSpec {
    val request: RequestSpec = RequestSpec()
    val response: ResponseSpec = ResponseSpec()
    val tags: List<String> get() = resource.tags
    val otherResponses get() = otherResponseCaseContext.responses

    private val resource: ResourceSpec = ResourceSpec(name, request, response)
    private val otherResponseCaseContext: ValidatelessResponseCaseContext = ValidatelessResponseCaseContext()

    fun information(specCustomizer: ResourceSpec.() -> Unit) {
        resource.apply(specCustomizer)
    }

    fun description(description: String) {
        resource.description(description)
    }

    fun tag(tag: String) {
        resource.tag(tag)
    }

    fun tags(vararg tags: String) {
        resource.tags(*tags)
    }

    fun tags(tags: Collection<String>) {
        resource.tags(tags)
    }

    fun request(specCustomizer: RequestSpec.() -> Unit) {
        request.apply(specCustomizer)
    }

    fun requestSchema(schema: String) {
        resource.requestSchema(schema)
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
        resource.responseSchema(schema)
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

    fun alternativeResponse(specCustomizer: ResponseSpec.() -> Unit) {
        otherResponseCaseContext.response(specCustomizer)
    }

    fun alternativeResponseCases(specCustomizer: ValidatelessResponseCaseContext.() -> Unit) {
        otherResponseCaseContext.apply(specCustomizer)
    }

    override fun build(): List<Snippet> {
        return buildList {
            addAll(request.build())
            addAll(response.build())
            addAll(resource.build())
        }
    }
}