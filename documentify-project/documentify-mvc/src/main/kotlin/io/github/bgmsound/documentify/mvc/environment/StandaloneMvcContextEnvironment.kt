package io.github.bgmsound.documentify.mvc.environment

import io.github.bgmsound.documentify.core.environment.AbstractStandaloneContextEnvironment
import io.github.bgmsound.documentify.mvc.MvcDocumentContextEnvironment
import org.springframework.format.support.FormattingConversionService
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.method.support.HandlerMethodArgumentResolver

class StandaloneMvcContextEnvironment private constructor(
    private val provider: RestDocumentationContextProvider
) : AbstractStandaloneContextEnvironment<StandaloneMvcContextEnvironment>(), MvcDocumentContextEnvironment {
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

    override fun buildMockMvc(): MockMvc {
        return MockMvcBuilders
            .standaloneSetup(*controllers.toTypedArray())
            .setControllerAdvice(*controllerAdvices.toTypedArray())
            .setCustomArgumentResolvers(*argumentResolvers.toTypedArray())
            .setConversionService(FormattingConversionService().apply {
                converters.forEach { converter ->
                    addConverter(converter)
                }
            })
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(provider))
            .apply { if (codec != null) {
                val objectMapper = codec!!
                setMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
            }}
            .build()
    }

    companion object {
        fun standaloneEnvironment(provider: RestDocumentationContextProvider): StandaloneMvcContextEnvironment {
            return StandaloneMvcContextEnvironment(provider)
        }
    }
}