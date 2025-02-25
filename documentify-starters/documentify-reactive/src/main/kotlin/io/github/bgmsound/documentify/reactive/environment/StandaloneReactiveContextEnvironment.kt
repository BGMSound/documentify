package io.github.bgmsound.documentify.reactive.environment

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.bgmsound.documentify.core.environment.StandaloneContextEnvironmentDelegate
import io.github.bgmsound.documentify.core.environment.StandaloneContextEnvironmentSpec
import io.github.bgmsound.documentify.reactive.ReactiveDocumentContextEnvironment
import org.springframework.http.MediaType
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.MockServerSpec
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.WebFilter


class StandaloneReactiveContextEnvironment private constructor(
    private val provider: RestDocumentationContextProvider,
    private val delegate: StandaloneContextEnvironmentDelegate<StandaloneReactiveContextEnvironment> = StandaloneContextEnvironmentDelegate()
) : StandaloneContextEnvironmentSpec<StandaloneReactiveContextEnvironment> by delegate, ReactiveDocumentContextEnvironment() {
    init {
        delegate.environmentSpec = this
    }

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

    override fun buildWebTestClient(): WebTestClient {
        return WebTestClient
            .bindToController(*delegate.controllers.toTypedArray())
            .controllerAdvice(*delegate.controllerAdvices.toTypedArray())
            .argumentResolvers { configurer ->
                configurer.addCustomResolver(*argumentResolvers.toTypedArray())
            }
            .httpMessageCodecs { configurer -> if (delegate.objectMapper != null) {
                val objectMapper = delegate.objectMapper!!
                configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON))
                configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
            }}
            .configureClient()
            .include(delegate.objectMapper)
            .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
            .build()
    }

    companion object {
        fun standaloneEnvironment(provider: RestDocumentationContextProvider): StandaloneReactiveContextEnvironment {
            return StandaloneReactiveContextEnvironment(provider)
        }
    }

    private fun WebTestClient.Builder.include(objectMapper: ObjectMapper?): WebTestClient.Builder {
        if (objectMapper == null) return this
        return exchangeStrategies(ExchangeStrategies.builder().codecs { configurer ->
            configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON))
            configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
        }.build())
    }
}