package kr.bgmsound.documentify.core

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.http.Method
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*

class RestDocsEmitter(
    private val document: DocumentSpec
) {
    fun emit(requestSpecification: RequestSpecification) {
        val snippets = document.build()
        val response = given(requestSpecification)
            .log().all()
            .pathParams(document.request.samplePathVariables())
            .queryParams(document.request.sampleQueryParameters())
            .headers(document.request.sampleHeaders())
        if (document.request.fields.isNotEmpty()) {
            response.body(document.request.sampleFields())
        }
        response
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
            .then()
            .log().all()
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