package io.github.bgmsound.documentify.core.specification.request

import io.github.bgmsound.documentify.core.specification.APISpec
import io.github.bgmsound.documentify.core.specification.element.SpecElement.Requirement
import io.github.bgmsound.documentify.core.specification.element.PathVariable
import io.github.bgmsound.documentify.core.specification.element.QueryParameter
import io.restassured.http.Method
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Snippet

class RequestLineSpec(
    var url: String,
    var method: Method,
    private val pathParameters: MutableList<PathVariable> = mutableListOf(),
    private val queryParameters: MutableList<QueryParameter> = mutableListOf(),
) : APISpec {
    fun pathVariables(): List<PathVariable> = pathParameters

    fun queryParameters(): List<QueryParameter> = queryParameters

    fun pathVariable(key: String, description: String, sample: String): PathVariable {
        val pathVariable = PathVariable.newPathVariable(key, description, sample, Requirement.REQUIRED)
        return putPathVariable(pathVariable)
    }

    fun optionalPathVariable(key: String, description: String, sample: String): PathVariable {
        val pathVariable = PathVariable.newPathVariable(key, description, sample, Requirement.OPTIONAL)
        return putPathVariable(pathVariable)
    }

    fun queryParameter(key: String, description: String, sample: String): QueryParameter {
        val queryParameter = QueryParameter.newQueryParameter(key, description, sample, Requirement.REQUIRED)
        return putQueryParameter(queryParameter)
    }

    fun optionalQueryParameter(key: String, description: String, sample: String): QueryParameter {
        val queryParameter = QueryParameter.newQueryParameter(key, description, sample, Requirement.OPTIONAL)
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
                val pathVariablesSnippet = RequestDocumentation
                    .pathParameters(
                        pathParameters.map { it.build() }
                    )
                add(pathVariablesSnippet!!)
            }
            if (queryParameters.isNotEmpty()) {
                val queryParametersSnippet = RequestDocumentation
                    .queryParameters(
                        queryParameters.map { it.build() }
                    )
                add(queryParametersSnippet!!)
            }
        }
    }
}