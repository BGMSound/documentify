package io.github.bgmsound.documentify.core.emitter


import io.github.bgmsound.documentify.core.environment.DocumentContextEnvironment
import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.springframework.http.HttpMethod
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

class WebTestClientDocumentEmitter(
    provider: RestDocumentationContextProvider,
    documentSpec: DocumentSpec,
    environment: DocumentContextEnvironment
) : AbstractDocumentEmitter(provider, documentSpec) {
    private val webTestClient: WebTestClient = environment.buildTestClient()
    private val requestPreprocessors = environment.requestPreprocessors().toTypedArray()
    private val responsePreprocessors = environment.responsePreprocessors().toTypedArray()

    override fun emitDocument(): BodyContentSpec {
        val snippets = documentSpec.build()
        val samplePathVariables = sampleAggregator.aggregate(documentSpec.request.pathVariables)
        val sampleHeaders = sampleAggregator.aggregate(documentSpec.request.headers)
        val sampleFields = sampleAggregator.aggregate(documentSpec.request.fields)

        return webTestClient
            .method(method())
            .uri(requestUri, samplePathVariables)
            .headers { headers ->
                headers.addAll(sampleHeaders.toMultiValueMap())
            }
            .bodyIfExist(sampleFields)
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
                    preprocessRequest(prettyPrint(), *requestPreprocessors),
                    preprocessResponse(prettyPrint(), *responsePreprocessors),
                    *snippets.toTypedArray()
                )
            )
    }

    override fun emitAlternativeResponseDocument() {
        documentSpec.otherResponses.forEachIndexed { index, response ->
            val sampleResponseFields = sampleAggregator.aggregate(response.fields)
            val api = AlternativeResponseDocumentController.new(
                response.statusCode,
                sampleResponseFields
            )
            val webTestClient = WebTestClient
                .bindToController(api)
                .configureClient()
                .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
                .build()

            val samplePathVariables = sampleAggregator.aggregate(documentSpec.request.pathVariables)
            webTestClient
                .method(method())
                .uri(requestUri, samplePathVariables)
                .exchange()
                .expectStatus()
                .isEqualTo(response.statusCode)
                .expectBody()
                .consumeWith { println(it)}
                .consumeWith(
                    document(
                        "${documentSpec.name}-case-${index + 1}",
                        preprocessRequest(prettyPrint(), *requestPreprocessors),
                        preprocessResponse(prettyPrint(), *responsePreprocessors),
                        response.buildResource(index)
                    )
                )
        }
    }

    private val requestUri get(): String {
        return StringBuilder().apply {
            append(documentSpec.request.url)
            if (documentSpec.request.queryParameters.isNotEmpty()) {
                append("?")
                append(documentSpec.request.queryParameters.joinToString("&") { parameter ->
                    "${parameter.key}=${parameter.sample}"
                })
            }
        }.toString()
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
}
