package kr.bgmsound.documentify.core.request

import io.restassured.http.Method
import kr.bgmsound.documentify.core.APISpec
import kr.bgmsound.documentify.core.PathVariable
import kr.bgmsound.documentify.core.QueryParameter
import kr.bgmsound.documentify.core.SAMPLE
import org.springframework.restdocs.request.ParameterDescriptor
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

    fun pathVariable(key: String, sample: String, description: String) {
        val descriptor = RequestDocumentation.parameterWithName(key)
            .description(description)
            .attributes(
                Attributes.Attribute(SAMPLE, sample)
            )
        pathParameters.add(PathVariable(descriptor))
    }

    fun queryParameter(key: String, sample: String, description: String) {
        val descriptor = RequestDocumentation.parameterWithName(key)
            .description(description)
            .attributes(
                Attributes.Attribute(SAMPLE, sample)
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