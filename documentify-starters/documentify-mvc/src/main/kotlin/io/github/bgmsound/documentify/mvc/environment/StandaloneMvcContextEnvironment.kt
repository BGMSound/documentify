package io.github.bgmsound.documentify.mvc.environment

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.bgmsound.documentify.core.environment.StandaloneContextEnvironmentDelegate
import io.github.bgmsound.documentify.core.environment.StandaloneContextEnvironmentSpec
import io.github.bgmsound.documentify.mvc.MvcDocumentContextEnvironment
import io.github.bgmsound.documentify.mvc.emitter.CustomRequestSerializer
import io.github.bgmsound.documentify.mvc.emitter.CustomResponseSerializer
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.method.support.HandlerMethodArgumentResolver

class StandaloneMvcContextEnvironment private constructor(
    private val provider: RestDocumentationContextProvider,
    private val delegate: StandaloneContextEnvironmentDelegate<StandaloneMvcContextEnvironment> = StandaloneContextEnvironmentDelegate()
) : StandaloneContextEnvironmentSpec<StandaloneMvcContextEnvironment> by delegate, MvcDocumentContextEnvironment() {
    init {
        delegate.environmentSpec = this
    }

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
            .standaloneSetup(*delegate.controllers.toTypedArray())
            .setControllerAdvice(*delegate.controllerAdvices.toTypedArray())
            .setCustomArgumentResolvers(*argumentResolvers.toTypedArray())
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(provider))
            .apply { if (delegate.objectMapper != null) {
                val objectMapper = delegate.objectMapper!!
                requestPreprocessors(CustomRequestSerializer(objectMapper))
                responsePreprocessors(CustomResponseSerializer(objectMapper))
            }}
            .build()
    }

    companion object {
        fun standaloneEnvironment(provider: RestDocumentationContextProvider): StandaloneMvcContextEnvironment {
            return StandaloneMvcContextEnvironment(provider)
        }
    }
}