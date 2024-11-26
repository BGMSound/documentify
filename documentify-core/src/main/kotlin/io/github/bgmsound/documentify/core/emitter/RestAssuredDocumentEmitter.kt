package io.github.bgmsound.documentify.core.emitter

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import io.github.bgmsound.documentify.core.documentation.specification.Method
import io.github.bgmsound.documentify.core.documentation.specification.document.DocumentSpec
import io.github.bgmsound.documentify.core.documentation.specification.response.ResponseSpec
import io.github.bgmsound.documentify.core.emitter.SpecElementAssociater.associatedFieldSample
import io.github.bgmsound.documentify.core.emitter.SpecElementAssociater.associatedSample
import io.github.bgmsound.documentify.core.environment.StandaloneContext.Companion.controller
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.response.MockMvcResponse
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.MockMvc


class RestAssuredDocumentEmitter(
    private val documentSpec: DocumentSpec
) : DocumentEmitter {
    override fun emit(
        provider: RestDocumentationContextProvider,
        mockMvc: MockMvc
    ) {
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
            .sendRequest()
        response
            .then()
            .log().all()
            .assertThat()
            .apply(documentResultHandler)
            .statusCode(documentSpec.response.statusCode)
        documentOtherResponses(provider)
    }

    private fun documentOtherResponses(provider: RestDocumentationContextProvider) {
        documentSpec.otherResponses.forEachIndexed { index, response ->
            val api = OtherResponseDocumentController.new(
                response.statusCode,
                response.fields.associatedFieldSample()
            )
            val mockMvc = controller(api).build(provider)
            val requestSpecification: MockMvcRequestSpecification = given().mockMvc(mockMvc)

            requestSpecification
                .pathParams(documentSpec.request.pathVariables.associatedSample())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .sendRequest()
                .then()
                .apply(
                    document(
                        "${documentSpec.name}-case-${index+1}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        response.buildResource(index)
                    )
                )
                .statusCode(response.statusCode)
        }
    }

    private fun ResponseSpec.buildResource(index: Int): Snippet {
        val resourceBuilder = ResourceSnippetParameters.builder()
        if (documentSpec.tags.isNotEmpty()) {
            resourceBuilder.tags(*documentSpec.tags.toTypedArray())
        }
        if (this.fields.isNotEmpty()) {
            if (this.schema == null) {
                resourceBuilder.responseSchema(Schema.schema("${documentSpec.name}-Response-case${index+1}"))
            } else {
                resourceBuilder.responseSchema(Schema.schema(this.schema!!))
            }
        }
        return ResourceDocumentation.resource(resourceBuilder.build())
    }

    private fun MockMvcRequestSpecification.bodyIfExists(
        fields: Map<String, Any>,
    ): MockMvcRequestSpecification = if (fields.isEmpty()) {
        this
    } else {
        body(fields)
    }

    private fun MockMvcRequestSpecification.sendRequest(): MockMvcResponse {
        return when (documentSpec.request.method) {
            Method.GET -> get(documentSpec.request.url)
            Method.POST -> post(documentSpec.request.url)
            Method.PUT -> put(documentSpec.request.url)
            Method.PATCH -> patch(documentSpec.request.url)
            Method.DELETE -> delete(documentSpec.request.url)
        }
    }
}