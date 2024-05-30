package kr.bgmsound.documentify.core

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.http.Method
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document

class RestDocsEmitter(
    private val document: DocumentSpec
) {
    fun emit(requestSpecification: RequestSpecification) {
        val snippets = document.build()
        val pathVariables = document.request.pathVariables.associate { it.key to it.sample }
        val queryParameters = document.request.queryParameters.associate { it.key to it.sample }
        val response = given(requestSpecification)
            .pathParams(pathVariables)
            .queryParams(queryParameters)
            .filter(
                document(
                    document.name,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    *snippets.toTypedArray()
                )
            )
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .sendRequest()
        response.prettyPrint()
        response.then()
            .statusCode(document.response.statusCode)
    }

    private fun RequestSpecification.sendRequest(): Response {
        return when (document.request.method) {
            Method.GET -> get(document.request.url)
            Method.POST -> post(document.request.url)
            Method.PUT -> put(document.request.url)
            Method.PATCH -> patch(document.request.url)
            Method.DELETE -> delete(document.request.url)
            else -> throw IllegalArgumentException("Unsupported method: ${document.request.method}")
        }
    }
}