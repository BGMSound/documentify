package io.github.bgmsound.documentify.core.emitter

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import io.github.bgmsound.documentify.core.documentation.specification.document.DocumentSpec
import io.github.bgmsound.documentify.core.emitter.SpecElementAssociater.associatedSample
import io.github.bgmsound.documentify.core.emitter.SpecElementAssociater.associatedFieldSample
import io.restassured.http.ContentType
import io.restassured.http.Method
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.response.MockMvcResponse
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.test.web.servlet.MockMvc


class RestAssuredDocumentEmitter(
    private val document: DocumentSpec
) : DocumentEmitter {
    override fun emit(mockMvc: MockMvc) {
        val snippets = document.build()
        val documentSpec = document(
            document.name,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            *snippets.toTypedArray()
        )
        val requestSpecification: MockMvcRequestSpecification = given().mockMvc(mockMvc)
        val response = requestSpecification
            .log().all()
            .pathParams(document.request.pathVariables.associatedSample())
            .queryParams(document.request.queryParameters.associatedSample())
            .headers(document.request.headers.associatedSample())
            .bodyIfExists(document.request.fields.associatedFieldSample())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .sendRequest()
        response
            .then()
            .log().all()
            .assertThat()
            .apply(documentSpec)
            .statusCode(document.response.statusCode)
    }

    private fun MockMvcRequestSpecification.bodyIfExists(
        fields: Map<String, Any>,
    ): MockMvcRequestSpecification = if (fields.isEmpty()) {
        this
    } else {
        body(fields)
    }

    private fun MockMvcRequestSpecification.sendRequest(): MockMvcResponse {
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