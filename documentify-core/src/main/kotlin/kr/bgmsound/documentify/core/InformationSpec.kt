package kr.bgmsound.documentify.core

import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import kr.bgmsound.documentify.core.request.RequestSpec
import kr.bgmsound.documentify.core.response.ResponseSpec
import org.springframework.restdocs.snippet.Snippet

class InformationSpec(
    private val request: RequestSpec,
    private val response: ResponseSpec,
) : APISpec {
    private val tags = mutableListOf<String>()
    private var summary: String? = null
    private var description: String? = null

    fun tag(tag: String) {
        tags.add(tag)
    }

    fun tags(tags: Collection<String>) {
        this.tags.addAll(tags)
    }

    fun summary(summary: String) {
        this.summary = summary
    }

    fun description(description: String) {
        this.description = description
    }

    override fun build(): List<Snippet> {
        val resourceBuilder = ResourceSnippetParameters.builder()
        if (tags.isNotEmpty()) {
            resourceBuilder.tags(*tags.toTypedArray())
        }
        summary?.let {
            resourceBuilder.summary(it)
        }
        description?.let {
            resourceBuilder.description(it)
        }
        if (request.pathVariables.isNotEmpty()) {
            resourceBuilder.pathParameters(*request.pathVariables.map { it.descriptor }.reversed().toTypedArray())
        }
        if (request.queryParameters.isNotEmpty()) {
            resourceBuilder.queryParameters(*request.queryParameters.map { it.descriptor }.reversed().toTypedArray())
        }
        if (request.headers.isNotEmpty()) {
            resourceBuilder.requestHeaders(*request.headers.map { it.descriptor }.reversed().toTypedArray())
        }
        if (response.headers.isNotEmpty()) {
            resourceBuilder.responseHeaders(*response.headers.map { it.descriptor }.reversed().toTypedArray())
        }
        resourceBuilder.requestFields(request.fields.map { it.descriptor })
        resourceBuilder.responseFields(response.fields.map { it.descriptor })
        return listOf(ResourceDocumentation.resource(resourceBuilder.build()))
    }
}