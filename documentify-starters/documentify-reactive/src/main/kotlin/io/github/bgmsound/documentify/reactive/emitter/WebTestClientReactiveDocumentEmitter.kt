package io.github.bgmsound.documentify.reactive.emitter


import io.github.bgmsound.documentify.core.emitter.SpecElementSampleAssociater.associatedFieldSample
import io.github.bgmsound.documentify.core.emitter.SpecElementSampleAssociater.associatedSample
import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.springframework.http.HttpMethod
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

class WebTestClientReactiveDocumentEmitter(
    provider: RestDocumentationContextProvider,
    documentSpec: DocumentSpec,
    private val webTestClient: WebTestClient,
) : AbstractReactiveDocumentEmitter(provider, documentSpec) {
    override suspend fun emitDocument() {
        val snippets = documentSpec.build()
        webTestClient
            .method(method())
            .uri(uri(), documentSpec.request.pathVariables.associatedSample())
            .headers { headers ->
                headers.addAll(documentSpec.request.headers.associatedSample().toMultiValueMap())
            }
            .bodyIfExist(documentSpec.request.fields.associatedSample())
            .exchange()
            .expectStatus()
            .isEqualTo(documentSpec.response.statusCode)
            .expectBody()
            .consumeWith {
                println(it)
            }
            .consumeWith(
                document(
                    documentSpec.name,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    *snippets.toTypedArray()
                )
            )
    }

    override suspend fun emitAlternativeResponseDocument() {
        documentSpec.otherResponses.forEachIndexed { index, response ->
            val api = AlternativeReactiveResponseDocumentController.new(
                response.statusCode,
                response.fields.associatedFieldSample()
            )
            val webTestClient = WebTestClient
                .bindToController(api)
                .configureClient()
                .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
                .build()
            webTestClient
                .method(method())
                .uri(uri(), documentSpec.request.pathVariables.associatedSample())
                .bodyIfExist(documentSpec.request.fields.associatedSample())
                .exchange()
                .expectStatus()
                .isEqualTo(response.statusCode)
                .expectBody()
                .consumeWith { println(it)}
                .consumeWith(
                    document(
                        "${documentSpec.name}-case-${index + 1}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        response.buildResource(index)
                    )
                )
        }
    }

    private fun RequestBodySpec.bodyIfExist(
        fields: Map<String, Any>,
    ): WebTestClient.RequestHeadersSpec<*> = if (fields.isEmpty()) {
        this
    } else {
        bodyValue(fields)
    }

    private fun method(): HttpMethod {
        return when (documentSpec.request.method) {
            Method.GET -> HttpMethod.GET
            Method.POST -> HttpMethod.POST
            Method.PUT -> HttpMethod.PUT
            Method.DELETE -> HttpMethod.DELETE
            Method.PATCH -> HttpMethod.PATCH
        }
    }

    private fun Map<String, Any>.toMultiValueMap(): MultiValueMap<String, String> {
        val linkedMultiValueMap = LinkedMultiValueMap<String, String>()
        this.forEach { (key, value) ->
            linkedMultiValueMap.add(key, value.toString())
        }
        return linkedMultiValueMap
    }

    private fun uri(): String {
        return StringBuilder().apply {
            append(documentSpec.request.url)
            if (documentSpec.request.queryParameters.isNotEmpty()) {
                append("?")
                append(documentSpec.request.queryParameters.joinToString("&") { it ->
                    "${it.key}=${it.sample}"
                })
            }
        }.toString()
    }
}
