package io.github.bgmsound.documentify.core

import io.restassured.http.ContentType
import io.restassured.http.Method
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import io.restassured.module.mockmvc.response.MockMvcResponse
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import org.hamcrest.Matchers.equalTo
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.bgmsound.documentify.core.specification.DocumentSpec
import io.github.bgmsound.documentify.core.specification.element.SpecElement

class RestDocsEmitter(
    private val document: DocumentSpec
) {
    private val objectMapper = ObjectMapper()

    fun emit(requestSpecification: MockMvcRequestSpecification) {
        val snippets = document.build()
        val documentSchema = document(
            document.name,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            *snippets.toTypedArray()
        )
        val response = requestSpecification
            .log().all()
            .pathParams(document.request.pathVariables.sample())
            .queryParams(document.request.queryParameters.sample())
            .headers(document.request.headers.sample())
            .bodyIfExists(document.request.fields.sample())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .sendRequest()
        response.prettyPrint()
        val validator = response
            .then()
            .log().all()
            .assertThat()
            .apply(documentSchema)
            .statusCode(document.response.statusCode)
        if (document.response.fields.isNotEmpty()) {
            validator.body(equalTo(document.response.fields.sample().toJson()))
        }
    }

    private fun MockMvcRequestSpecification.bodyIfExists(
        fields: Map<String, Any>
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

    private fun List<SpecElement>.sample(): Map<String, Any> {
        return associate { it.key to it.sample }
    }

    private fun Map<String, Any>.toJson(): String = objectMapper.writeValueAsString(this)
}