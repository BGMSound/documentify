package kr.bgmsound.documentify.core

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.http.Method
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document

class RestDocsEmitter(
    private val documentSpec: DocumentSpec
) {
    fun emit(requestSpecification: RequestSpecification) {
        val snippets = documentSpec.build()
        val response = given(requestSpecification)
            .filter(
                document(
                    documentSpec.name,
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
            .statusCode(documentSpec.statusCode)
    }

    private fun RequestSpecification.sendRequest(): Response {
        return when (documentSpec.method) {
            Method.GET -> get(documentSpec.url)
            Method.POST -> post(documentSpec.url)
            Method.PUT -> put(documentSpec.url)
            Method.PATCH -> patch(documentSpec.url)
            Method.DELETE -> delete(documentSpec.url)
            else -> throw IllegalArgumentException("Unsupported method: ${documentSpec.method}")
        }
    }
}