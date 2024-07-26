package io.github.bgmsound.documentify.core.specification

import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import io.github.bgmsound.documentify.core.specification.request.RequestSpec
import io.github.bgmsound.documentify.core.specification.response.ResponseSpec
import org.springframework.restdocs.snippet.Snippet

class InformationSpec(
    documentName: String,
    private val request: RequestSpec,
    private val response: ResponseSpec
) : APISpec {
    private val tags = mutableListOf<String>()
    private var summary: String
    private var description: String = ""
    private var requestSchema: String
    private var responseSchema: String

    init {
        summary = documentName
        requestSchema = "${documentName}-request"
        responseSchema = "${documentName}-response"
    }

    fun tag(tag: String) {
        tags.add(tag)
    }

    fun tags(tags: Collection<String>) {
        this.tags.addAll(tags)
    }

    fun tags(vararg tags: String) {
        this.tags.addAll(tags)
    }

    fun summary(summary: String) {
        this.summary = summary
    }

    fun description(description: String) {
        this.description = description
    }

    fun requestSchema(schema: String) {
        this.requestSchema = schema
    }

    fun responseSchema(schema: String) {
        this.responseSchema = schema
    }

    override fun build(): List<Snippet> {
        val resourceBuilder = ResourceSnippetParameters.builder()
        if (tags.isNotEmpty()) {
            resourceBuilder.tags(*tags.toTypedArray())
        }
        resourceBuilder.summary(summary)
        resourceBuilder.description(description)
        if (request.pathVariables.isNotEmpty()) {
            resourceBuilder.pathParameters(*request.pathVariables.map { it.descriptor }.toTypedArray())
        }
        if (request.queryParameters.isNotEmpty()) {
            resourceBuilder.queryParameters(*request.queryParameters.map { it.descriptor }.toTypedArray())
        }
        if (request.headers.isNotEmpty()) {
            resourceBuilder.requestHeaders(*request.headers.map { it.descriptor }.toTypedArray())
        }
        if (response.headers.isNotEmpty()) {
            resourceBuilder.responseHeaders(*response.headers.map { it.descriptor }.toTypedArray())
        }
        if (request.fields.isNotEmpty()) {
            resourceBuilder.requestFields(request.fields.map { it.descriptor })
            resourceBuilder.requestSchema(Schema.schema(requestSchema))
        }
        if (response.fields.isNotEmpty()) {
            resourceBuilder.responseFields(response.fields.map { it.descriptor })
            resourceBuilder.responseSchema(Schema.schema(responseSchema))
        }
        return listOf(ResourceDocumentation.resource(resourceBuilder.build()))
    }
}