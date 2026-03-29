package io.github.bgmsound.documentify.core.specification.schema

import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import io.github.bgmsound.documentify.core.specification.DocumentableSpec
import io.github.bgmsound.documentify.core.specification.element.Link
import io.github.bgmsound.documentify.core.specification.element.field.Field
import io.github.bgmsound.documentify.core.specification.schema.request.RequestSpec
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.snippet.Snippet

class ResourceSpec(
    documentName: String,
    private val request: RequestSpec,
    private val response: ResponseSpec
) : DocumentableSpec {
    val tags = mutableListOf<String>()
    private val links = mutableListOf<Link>()
    private var summary: String
    private var description: String = ""

    init {
        summary = ""
        val randomSuffix = randomSuffix()
        request.schema = "$documentName Request ($randomSuffix)"
        response.schema = "$documentName Response ($randomSuffix)"
    }

    fun link(rel: String): Link {
        val link = Link.newLink(rel)
        this.links.add(link)
        return link
    }

    fun links(vararg links: String) {
        this.links.addAll(links.map { Link.newLink(it) })
    }

    fun links(links: Collection<String>) {
        this.links.addAll(links.map { Link.newLink(it) })
    }

    fun tag(tag: String) {
        this.tags.add(tag)
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
        request.schema = schema
    }

    fun responseSchema(schema: String) {
        response.schema = schema
    }

    override fun build(): List<Snippet> {
        val resourceBuilder = ResourceSnippetParameters.Companion.builder()
        if (tags.isNotEmpty()) {
            resourceBuilder.tags(*tags.toTypedArray())
        }
        resourceBuilder.summary(summary)
        resourceBuilder.description(description)
        if (links.isNotEmpty()) {
            resourceBuilder.links(*links.map { it.build() }.toTypedArray())
        }
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
                resourceBuilder.requestSchema(Schema.schema(request.schema!!))
            }
            resourceBuilder.requestFields(buildFields(request.fields))
        }
        if (response.fields.isNotEmpty()) {
            if (response.schema != null) {
                resourceBuilder.responseSchema(Schema.schema(response.schema!!))
            }
            resourceBuilder.responseFields(buildFields(response.fields))
        }
        return listOf(ResourceDocumentation.resource(resourceBuilder.build()))
    }

    private fun buildFields(fields: List<Field>): List<FieldDescriptor> {
        return buildList {
            fields.forEach { addAll(it.build()) }
        }
    }

    private fun randomSuffix(): String {
        return (1..5).map {
            val char = ('0'..'9').random()
            val number = ('a'..'z').random()
            (0..1).random().let { if (it == 0) char else number }
        }.joinToString("")
    }
}