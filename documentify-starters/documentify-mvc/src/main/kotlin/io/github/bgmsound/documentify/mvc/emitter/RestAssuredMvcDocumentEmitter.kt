package io.github.bgmsound.documentify.mvc.emitter

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import io.github.bgmsound.documentify.core.emitter.FieldJsonMatcherAssociater.associatedMatchers
import io.github.bgmsound.documentify.core.emitter.SpecElementSampleAssociater.associatedFieldSample
import io.github.bgmsound.documentify.core.emitter.SpecElementSampleAssociater.associatedSample
import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.response.MockMvcResponse
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import org.hamcrest.Matchers
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

class RestAssuredMvcDocumentEmitter(
    provider: RestDocumentationContextProvider,
    documentSpec: DocumentSpec,
    private val mockMvc: MockMvc
) : AbstractMvcDocumentEmitter(provider, documentSpec) {
    override fun emitDocument() {
        val snippets = documentSpec.build()
        val documentResultHandler = document(
            documentSpec.name,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            *snippets.toTypedArray()
        )
        val requestSpecification: MockMvcRequestSpecification = given().mockMvc(mockMvc)
        val response = requestSpecification
            .log().all()
            .pathParams(documentSpec.request.pathVariables.associatedSample())
            .queryParams(documentSpec.request.queryParameters.associatedSample())
            .headers(documentSpec.request.headers.associatedSample())
            .bodyIfExists(documentSpec.request.fields.associatedFieldSample())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .request()
        response
            .then()
            .log().all()
            .assertThat()
            .apply(documentResultHandler)
            .statusCode(documentSpec.response.statusCode)
            .validateExpectPayload()
    }

    override fun emitAlternativeResponseDocument() {
        documentSpec.otherResponses.forEachIndexed { index, response ->
            val api = AlternativeMvcResponseDocumentController.new(
                response.statusCode,
                response.fields.associatedFieldSample()
            )
            val mockMvc = MockMvcBuilders
                .standaloneSetup(api)
                .apply<StandaloneMockMvcBuilder>(documentationConfiguration(provider))
                .build()
            val requestSpecification: MockMvcRequestSpecification = given().mockMvc(mockMvc)

            requestSpecification
                .pathParams(documentSpec.request.pathVariables.associatedSample())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .request()
                .then()
                .apply(
                    document(
                        "${documentSpec.name}-case-${index + 1}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        response.buildResource(index)
                    )
                )
                .statusCode(response.statusCode)
        }
    }

    private fun MockMvcRequestSpecification.bodyIfExists(
        fields: Map<String, Any>
    ): MockMvcRequestSpecification = if (fields.isEmpty()) {
        this
    } else {
        body(fields)
    }

    private fun MockMvcRequestSpecification.request(): MockMvcResponse {
        return when (documentSpec.request.method) {
            Method.GET -> get(documentSpec.request.url)
            Method.POST -> post(documentSpec.request.url)
            Method.PUT -> put(documentSpec.request.url)
            Method.PATCH -> patch(documentSpec.request.url)
            Method.DELETE -> delete(documentSpec.request.url)
        }
    }

    private fun ValidatableMockMvcResponse.validateExpectPayload(): ValidatableMockMvcResponse {
        val matchers = documentSpec.response.fields.associatedMatchers()
        for ((key, value) in matchers) {
            if (key.contains("[*]")) {
                expect(jsonPath(key).value(Matchers.hasItem(value)))
            } else {
                expect(jsonPath(key).value(value))
            }
        }
        return this
    }

    private fun String.isListItem(): Boolean {
        val splitResult = this.split(".")
        val lastIndex = splitResult.lastIndex
        return (splitResult.size > 1 && splitResult[lastIndex - 1].endsWith("[]"))
    }
}