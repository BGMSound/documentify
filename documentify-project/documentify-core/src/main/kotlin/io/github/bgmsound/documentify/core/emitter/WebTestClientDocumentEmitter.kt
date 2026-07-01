package io.github.bgmsound.documentify.core.emitter


import io.github.bgmsound.documentify.core.PrintOption
import io.github.bgmsound.documentify.core.environment.DocumentContextEnvironment
import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.slf4j.LoggerFactory
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
    environment: DocumentContextEnvironment,
    private val printOption: PrintOption
) : AbstractDocumentEmitter(provider, documentSpec) {
    private val webTestClient: WebTestClient = environment.buildTestClient()
    private val requestPreprocessors = environment.requestPreprocessors().toTypedArray()
    private val responsePreprocessors = environment.responsePreprocessors().toTypedArray()
    private val log = LoggerFactory.getLogger(WebTestClientDocumentEmitter::class.java)

    override fun emitDocument(): BodyContentSpec {
        val snippets = documentSpec.build()
        val samplePathVariables = sampleAggregator.aggregate(documentSpec.request.pathVariables)
        val sampleHeaders = sampleAggregator.aggregate(documentSpec.request.headers)
        val sampleFields = sampleAggregator.aggregate(documentSpec.request.fields)
        val (uriTemplate, queryVariables) = requestUri()

        return webTestClient
            .method(method())
            .uri(uriTemplate, samplePathVariables + queryVariables)
            .headers { headers ->
               sampleHeaders.forEach { (key, value) ->
                   headers.add(key, value.toString())
               }
            }
            .bodyIfExist(sampleFields)
            .exchange()
            .expectStatus()
            .isEqualTo(documentSpec.response.statusCode)
            .expectBody()
            .consumeWith {
                if (printOption == PrintOption.ON) {
                    log.info("\n{}", it)
                }
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
        val samplePathVariables = sampleAggregator.aggregate(documentSpec.request.pathVariables)
        val (uriTemplate, queryVariables) = requestUri()
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
            val snippets = response.build()
            webTestClient
                .method(method())
                .uri(uriTemplate, samplePathVariables + queryVariables)
                .exchange()
                .expectStatus()
                .isEqualTo(response.statusCode)
                .expectBody()
                .consumeWith {
                    if (printOption == PrintOption.ON) log.info("\n{}", it)
                }
                .consumeWith(
                    document(
                        "${documentSpec.name}-case-${index + 1}",
                        preprocessRequest(prettyPrint(), *requestPreprocessors),
                        preprocessResponse(prettyPrint(), *responsePreprocessors),
                        *(snippets + listOf(response.buildResource(index))).toTypedArray()
                    )
                )
        }
    }

    private fun requestUri(): Pair<String, Map<String, Any>> {
        val queryVariables = mutableMapOf<String, Any>()
        val template = StringBuilder(documentSpec.request.url)
        documentSpec.request.queryParameters.forEachIndexed { index, parameter ->
            val placeholder = "documentifyQuery$index"
            template.append(if (index == 0) "?" else "&")
            template.append("${parameter.key}={$placeholder}")
            queryVariables[placeholder] = parameter.sample
        }
        return template.toString() to queryVariables
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
