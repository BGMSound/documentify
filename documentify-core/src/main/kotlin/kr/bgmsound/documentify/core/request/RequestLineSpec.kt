package kr.bgmsound.documentify.core.request

import io.restassured.http.Method
import kr.bgmsound.documentify.core.APISpec
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.request.QueryParametersSnippet
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Attributes
import org.springframework.restdocs.snippet.Snippet

class RequestLineSpec(
    var url: String,
    var method: Method,
    private val pathParameters: MutableList<ParameterDescriptor> = mutableListOf(),
    private val queryParameters: MutableList<ParameterDescriptor> = mutableListOf()
) : APISpec {

    private var pathVariablesSnippet: PathParametersSnippet? = null
    private var queryParametersSnippet: QueryParametersSnippet? = null

    fun pathVariable(key: String, sample: String, description: String) {
        pathParameters.add(
            RequestDocumentation.parameterWithName(key)
                .description(description)
                .attributes(
                    Attributes.Attribute("sample", sample)
                )
        )
    }

    fun queryParameter(key: String, sample: String, description: String) {
        queryParameters.add(
            RequestDocumentation.parameterWithName(key)
                .description(description)
                .attributes(
                    Attributes.Attribute("sample", sample)
                )
        )
    }

    override fun build(): List<Snippet> {
        return buildList {
            if (pathParameters.isNotEmpty()) {
                pathVariablesSnippet = RequestDocumentation.pathParameters(pathParameters)
                add(pathVariablesSnippet!!)
            }
            if (queryParameters.isNotEmpty()) {
                queryParametersSnippet = RequestDocumentation.queryParameters(queryParameters)
                add(queryParametersSnippet!!)
            }
        }
    }
}