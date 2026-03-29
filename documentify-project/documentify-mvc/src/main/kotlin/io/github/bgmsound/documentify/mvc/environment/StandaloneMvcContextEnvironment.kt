package io.github.bgmsound.documentify.mvc.environment

import io.github.bgmsound.documentify.core.environment.AbstractStandaloneContextEnvironment
import org.springframework.boot.convert.ApplicationConversionService
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.method.support.HandlerMethodArgumentResolver

class StandaloneMvcContextEnvironment private constructor(
    private val provider: RestDocumentationContextProvider
) : AbstractStandaloneContextEnvironment<StandaloneMvcContextEnvironment>() {
    private val argumentResolvers: MutableList<HandlerMethodArgumentResolver> = mutableListOf()

    fun argumentResolver(argumentResolver: HandlerMethodArgumentResolver): StandaloneMvcContextEnvironment {
        this.argumentResolvers.add(argumentResolver)
        return this
    }

    fun argumentResolvers(vararg argumentResolvers: HandlerMethodArgumentResolver): StandaloneMvcContextEnvironment {
        this.argumentResolvers.addAll(argumentResolvers)
        return this
    }

    fun argumentResolvers(argumentResolvers: List<HandlerMethodArgumentResolver>): StandaloneMvcContextEnvironment {
        this.argumentResolvers.addAll(argumentResolvers)
        return this
    }

    override fun buildTestClient(): WebTestClient {
        val mockMvc = MockMvcBuilders
            .standaloneSetup(*controllers.toTypedArray())
            .setControllerAdvice(*controllerAdvices.toTypedArray())
            .setCustomArgumentResolvers(*argumentResolvers.toTypedArray())
            .setConversionService(ApplicationConversionService().apply {
                converters.forEach { converter ->
                    addConverter(converter)
                }
            })
            .apply { if (codec != null) {
                val objectMapper = codec!!
                setMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
            }}
            .build()
        return MockMvcWebTestClient
            .bindTo(mockMvc)
            .filter(documentationConfiguration(provider))
            .build()
    }

    companion object {
        fun standaloneEnvironment(provider: RestDocumentationContextProvider): StandaloneMvcContextEnvironment {
            return StandaloneMvcContextEnvironment(provider)
        }
    }
}