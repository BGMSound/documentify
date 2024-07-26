package io.github.bgmsound.documentify.core.specification.request

import io.github.bgmsound.documentify.core.specification.APISpec
import io.github.bgmsound.documentify.core.specification.element.SpecElement.Type
import io.github.bgmsound.documentify.core.specification.element.PathVariable
import io.github.bgmsound.documentify.core.specification.element.QueryParameter
import io.restassured.http.Method
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.request.QueryParametersSnippet
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Snippet

class RequestLineSpec(
    var url: String,
    var method: Method,
    private val pathParameters: MutableList<PathVariable> = mutableListOf(),
    private val queryParameters: MutableList<QueryParameter> = mutableListOf(),
) : APISpec {

    private var pathVariablesSnippet: PathParametersSnippet? = null
    private var queryParametersSnippet: QueryParametersSnippet? = null

    fun pathVariables(): List<PathVariable> = pathParameters
    fun queryParameters(): List<QueryParameter> = queryParameters

    fun pathVariable(key: String, description: String, sample: String): PathVariable {
        val pathVariable = PathVariable.newPathVariable(key, description, sample, Type.REQUIRED)
        return putPathVariable(pathVariable)
    }

    fun optionalPathVariable(key: String, description: String, sample: String): PathVariable {
        val pathVariable = PathVariable.newPathVariable(key, description, sample, Type.OPTIONAL)
        return putPathVariable(pathVariable)
    }

    fun queryParameter(key: String, description: String, sample: String): QueryParameter {
        val queryParameter = QueryParameter.newQueryParameter(key, description, sample, Type.REQUIRED)
        return putQueryParameter(queryParameter)
    }

    fun optionalQueryParameter(key: String, description: String, sample: String): QueryParameter {
        val queryParameter = QueryParameter.newQueryParameter(key, description, sample, Type.OPTIONAL)
        return putQueryParameter(queryParameter)
    }

    private fun putPathVariable(pathVariable: PathVariable): PathVariable {
        pathParameters.add(pathVariable)
        return pathVariable
    }

    private fun putQueryParameter(queryParameter: QueryParameter): QueryParameter {
        queryParameters.add(queryParameter)
        return queryParameter
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