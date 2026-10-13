package io.github.bgmsound.documentify.reactive.environment

import io.github.bgmsound.documentify.core.environment.AbstractStandaloneContextEnvironment
import org.springframework.http.MediaType
import org.springframework.http.codec.json.JacksonJsonDecoder
import org.springframework.http.codec.json.JacksonJsonEncoder
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import tools.jackson.databind.json.JsonMapper


class StandaloneReactiveContextEnvironment private constructor(
    private val provider: RestDocumentationContextProvider
) : AbstractStandaloneContextEnvironment<StandaloneReactiveContextEnvironment>() {
    private val argumentResolvers = mutableListOf<HandlerMethodArgumentResolver>()

    fun argumentResolver(argumentResolver: HandlerMethodArgumentResolver): StandaloneReactiveContextEnvironment {
        this.argumentResolvers.add(argumentResolver)
        return this
    }

    fun argumentResolvers(vararg argumentResolvers: HandlerMethodArgumentResolver): StandaloneReactiveContextEnvironment {
        this.argumentResolvers.addAll(argumentResolvers)
        return this
    }

    fun argumentResolvers(argumentResolvers: List<HandlerMethodArgumentResolver>): StandaloneReactiveContextEnvironment {
        this.argumentResolvers.addAll(argumentResolvers)
        return this
    }

    override fun buildTestClient(): WebTestClient {
        return WebTestClient
            .bindToController(*controllers.toTypedArray())
            .controllerAdvice(*controllerAdvices.toTypedArray())
            .argumentResolvers { configurer ->
                configurer.addCustomResolver(*argumentResolvers.toTypedArray())
            }
            .formatters { registry ->
                converters.forEach { converter ->
                    registry.addConverter(converter)
                }
            }
            .httpMessageCodecs { configurer -> if (codec != null) {
                val objectMapper = codec!!
                configurer.defaultCodecs().jacksonJsonDecoder(JacksonJsonDecoder(objectMapper, MediaType.APPLICATION_JSON))
                configurer.defaultCodecs().jacksonJsonEncoder(JacksonJsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
            }}
            .configureClient()
            .include(codec)
            .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
            .build()
    }

    private fun WebTestClient.Builder.include(objectMapper: JsonMapper?): WebTestClient.Builder {
        if (objectMapper == null) return this
        return exchangeStrategies(ExchangeStrategies.builder().codecs { configurer ->
            configurer.defaultCodecs().jacksonJsonDecoder(JacksonJsonDecoder(objectMapper, MediaType.APPLICATION_JSON))
            configurer.defaultCodecs().jacksonJsonEncoder(JacksonJsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
        }.build())
    }

    companion object {
        fun standaloneEnvironment(provider: RestDocumentationContextProvider): StandaloneReactiveContextEnvironment {
            return StandaloneReactiveContextEnvironment(provider)
        }
    }
}