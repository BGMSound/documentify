package kr.bgmsound.documentify.core.request

import io.restassured.http.Method
import kr.bgmsound.documentify.core.Spec
import org.springframework.http.HttpStatus
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
) : Spec {

    private var _pathVariablesSnippet: PathParametersSnippet? = null
    private var _queryParametersSnippet: QueryParametersSnippet? = null

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
        _pathVariablesSnippet = RequestDocumentation.pathParameters(pathParameters)
        _queryParametersSnippet = RequestDocumentation.queryParameters(queryParameters)
        return listOf(_pathVariablesSnippet!!, _queryParametersSnippet!!)
    }
}