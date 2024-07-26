package io.github.bgmsound.documentify.core.specification.request

import io.github.bgmsound.documentify.core.specification.APISpec
import io.github.bgmsound.documentify.core.specification.SAMPLE_KEY
import io.github.bgmsound.documentify.core.specification.element.PathVariable
import io.github.bgmsound.documentify.core.specification.element.QueryParameter
import io.restassured.http.Method
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.request.QueryParametersSnippet
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Attributes
import org.springframework.restdocs.snippet.Snippet

class RequestLineSpec(
    var url: String,
    var method: Method,
    private val pathParameters: MutableList<PathVariable> = mutableListOf(),
    private val queryParameters: MutableList<QueryParameter> = mutableListOf()
) : APISpec {

    private var pathVariablesSnippet: PathParametersSnippet? = null
    private var queryParametersSnippet: QueryParametersSnippet? = null

    fun pathVariables(): List<PathVariable> = pathParameters
    fun queryParameters(): List<QueryParameter> = queryParameters

    fun pathVariable(key: String, description: String, sample: String) {
        val descriptor = RequestDocumentation.parameterWithName(key)
            .description(description)
            .attributes(
                Attributes.Attribute(SAMPLE_KEY, sample)
            )
        pathParameters.add(PathVariable(descriptor))
    }

    fun queryParameter(key: String, description: String, sample: String) {
        val descriptor = RequestDocumentation.parameterWithName(key)
            .description(description)
            .attributes(
                Attributes.Attribute(SAMPLE_KEY, sample)
            )
        queryParameters.add(QueryParameter(descriptor))
    }

    override fun build(): List<Snippet> {
        return buildList {
            if (pathParameters.isNotEmpty()) {
                pathVariablesSnippet = RequestDocumentation.pathParameters(pathParameters.map { it.descriptor })
                add(pathVariablesSnippet!!)
            }
            if (queryParameters.isNotEmpty()) {
                queryParametersSnippet = RequestDocumentation.queryParameters(queryParameters.map { it.descriptor })
                add(queryParametersSnippet!!)
            }
        }
    }
}