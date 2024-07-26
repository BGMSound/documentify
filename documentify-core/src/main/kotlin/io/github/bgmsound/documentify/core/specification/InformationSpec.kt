package io.github.bgmsound.documentify.core.specification

import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import io.github.bgmsound.documentify.core.specification.element.Field
import io.github.bgmsound.documentify.core.specification.request.RequestSpec
import io.github.bgmsound.documentify.core.specification.response.ResponseSpec
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.snippet.Snippet

class InformationSpec(
    documentName: String,
    private val request: RequestSpec,
    private val response: ResponseSpec
) : APISpec {
    private val tags = mutableListOf<String>()
    private var summary: String
    private var description: String = ""
    private var baseRequestSchema: String
    private var baseResponseSchema: String

    init {
        summary = documentName
        baseRequestSchema = "$documentName Request"
        baseResponseSchema = "$documentName Response"
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
        this.baseRequestSchema = schema
    }

    fun responseSchema(schema: String) {
        this.baseResponseSchema = schema
    }

    override fun build(): List<Snippet> {
        val resourceBuilder = ResourceSnippetParameters.builder()
        if (tags.isNotEmpty()) {
            resourceBuilder.tags(*tags.toTypedArray())
        }
        resourceBuilder.summary(summary)
        resourceBuilder.description(description)
        if (request.pathVariables.isNotEmpty()) {
            resourceBuilder.pathParameters(*request.pathVariables.map { it.build() }.toTypedArray())
        }
        if (request.queryParameters.isNotEmpty()) {
            resourceBuilder.queryParameters(*request.queryParameters.map { it.build() }.toTypedArray())
        }
        if (request.headers.isNotEmpty()) {
            resourceBuilder.requestHeaders(*request.headers.map { it.build() }.toTypedArray())
        }
        if (response.headers.isNotEmpty()) {
            resourceBuilder.responseHeaders(*response.headers.map { it.build() }.toTypedArray())
        }
        if (request.fields.isNotEmpty()) {
            if (request.schema != null) {
                baseRequestSchema = request.schema!!
            }
            resourceBuilder.requestFields(buildFields(request.fields))
            resourceBuilder.requestSchema(Schema.schema(baseRequestSchema))
        }
        if (response.fields.isNotEmpty()) {
            if (response.schema != null) {
                baseResponseSchema = response.schema!!
            }
            resourceBuilder.responseFields(buildFields(response.fields))
            resourceBuilder.responseSchema(Schema.schema(baseResponseSchema))
        }
        return listOf(ResourceDocumentation.resource(resourceBuilder.build()))
    }

    private fun buildFields(fields: List<Field>): List<FieldDescriptor> {
        return buildList {
            fields.forEach { addAll(it.build()) }
        }
    }
}