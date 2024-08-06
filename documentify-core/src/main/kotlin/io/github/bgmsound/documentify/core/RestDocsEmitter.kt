package io.github.bgmsound.documentify.core

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import io.github.bgmsound.documentify.core.specification.DocumentSpec
import io.github.bgmsound.documentify.core.specification.element.Field
import io.github.bgmsound.documentify.core.specification.element.SpecElement
import io.restassured.http.ContentType
import io.restassured.http.Method
import io.restassured.module.mockmvc.response.MockMvcResponse
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import java.time.LocalDate
import java.time.LocalDateTime


class RestDocsEmitter(
    private val document: DocumentSpec
) {
    fun emit(requestSpecification: MockMvcRequestSpecification) {
        val snippets = document.build()
        val documentSpec = document(
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
            .bodyIfExists(document.request.fields.associate())
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
        return associate {
            it.key to it.sample
        }
    }

    private fun List<Field>.associate(): Map<String, Any> {
        return filter { it.hasSample() || it.canHaveChild() || !it.isIgnored() }.associate { it.associate() }
    }

    private fun Field.associate(): Pair<String, Any> {
        if (isIgnored()) {
            throw IllegalStateException("can't associate ignored field $key")
        }
        if (!hasSample() && !canHaveChild()) {
            throw IllegalStateException("Field $key must have sample or child fields")
        }
        return key to if (hasSample()) {
            if (sample is LocalDate || sample is LocalDateTime) {
                sample.toString()
            } else {
                sample
            }
        } else {
            if (childFields().isEmpty()) {
                throw IllegalStateException("Field $key must have child fields")
            }
            val sample = childFields().associate { it.associate() }
            if (isArray()) {
                listOf(sample)
            } else {
                sample
            }
        }
    }
}