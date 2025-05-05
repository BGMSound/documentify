package io.github.bgmsound.documentify.mvc.emitter

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import io.github.bgmsound.documentify.core.emitter.FieldJsonMatcherAssociater.associatedMatchers
import io.github.bgmsound.documentify.core.emitter.SpecElementSampleAssociater.associatedFieldSample
import io.github.bgmsound.documentify.core.emitter.SpecElementSampleAssociater.associatedSample
import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.mvc.MvcDocumentContextEnvironment
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.response.MockMvcResponse
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import org.hamcrest.Matchers
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

class RestAssuredMvcDocumentEmitter(
    provider: RestDocumentationContextProvider,
    documentSpec: DocumentSpec,
    environment: MvcDocumentContextEnvironment
) : AbstractMvcDocumentEmitter(provider, documentSpec) {
    private val mockMvc = environment.buildMockMvc()
    private val requestPreprocessors = environment.requestPreprocessors().toTypedArray()
    private val responsePreprocessors = environment.responsePreprocessors().toTypedArray()

    override fun emitDocument(): ValidatableMockMvcResponse {
        val snippets = documentSpec.build()
        val samplePathVariables = sampleAggregator.aggregate(documentSpec.request.pathVariables)
        val sampleQueryParameters = sampleAggregator.aggregate(documentSpec.request.queryParameters)
        val sampleHeaders = sampleAggregator.aggregate(documentSpec.request.headers)
        val sampleFields = sampleAggregator.aggregate(documentSpec.request.fields)

        val documentResultHandler = document(
            documentSpec.name,
            preprocessRequest(prettyPrint(), *requestPreprocessors),
            preprocessResponse(prettyPrint(), *responsePreprocessors),
            *snippets.toTypedArray()
        )
        val requestSpecification: MockMvcRequestSpecification = given().mockMvc(mockMvc)
        val response = requestSpecification
            .log().all()
            .pathParams(samplePathVariables)
            .queryParams(sampleQueryParameters)
            .headers(sampleHeaders)
            .bodyIfExists(sampleFields)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .request()
        return response
            .then()
            .log().all()
            .assertThat()
            .apply(documentResultHandler)
            .statusCode(documentSpec.response.statusCode)
            .validateExpectPayload()
    }

    override fun emitAlternativeResponseDocument() {
        documentSpec.otherResponses.forEachIndexed { index, response ->
            val sampleResponseFields = sampleAggregator.aggregate(response.fields)
            val api = AlternativeMvcResponseDocumentController.new(
                response.statusCode,
                sampleResponseFields
            )
            val mockMvc = MockMvcBuilders
                .standaloneSetup(api)
                .apply<StandaloneMockMvcBuilder>(documentationConfiguration(provider))
                .build()
            val requestSpecification: MockMvcRequestSpecification = given().mockMvc(mockMvc)
            val samplePathVariables = sampleAggregator.aggregate(documentSpec.request.pathVariables)
            requestSpecification
                .pathParams(samplePathVariables)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .request()
                .then()
                .apply(
                    document(
                        "${documentSpec.name}-case-${index + 1}",
                        preprocessRequest(prettyPrint(), *requestPreprocessors),
                        preprocessResponse(prettyPrint(), *responsePreprocessors),
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
            if (key.endsWith("[*]")) {
                if (value !is List<*>) {
                    throw IllegalArgumentException("sample value type must be List")
                }
                expect(jsonPath(key.substringBeforeLast("[*]")).value(Matchers.containsInAnyOrder(*value.toTypedArray())))
            } else if (key.contains("[*]") && !key.endsWith("[*]")) {
                expect(jsonPath(key).value(Matchers.hasItem(value)))
            } else {
                expect(jsonPath(key).value(value))
            }
        }
        return this
    }
}