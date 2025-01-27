package io.github.bgmsound.documentify.mvc.environment

import io.github.bgmsound.documentify.core.environment.AbstractStandaloneContextEnvironment
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.mvc.MvcDocumentContextEnvironment
import io.github.bgmsound.documentify.mvc.emitter.EmitterFactory
import io.github.bgmsound.documentify.mvc.emitter.MvcDocumentEmitter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.method.support.HandlerMethodArgumentResolver

class StandaloneMvcContextEnvironment private constructor(
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

    override fun buildEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec,
    ): MvcDocumentEmitter {
        val mockMvc = MockMvcBuilders
            .standaloneSetup(*controllers.toTypedArray())
            .setControllerAdvice(*controllerAdvices.toTypedArray())
            .setCustomArgumentResolvers(*argumentResolvers.toTypedArray())
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(provider))
            .build()
        return EmitterFactory.createMvcEmitter(provider, documentSpec, mockMvc)
    }

    companion object {
        fun controller(controller: Any): StandaloneMvcContextEnvironment {
            return StandaloneMvcContextEnvironment().controller(controller)
        }

        fun controllers(vararg controllers: Any): StandaloneMvcContextEnvironment {
            return StandaloneMvcContextEnvironment().controllers(*controllers)
        }

        fun controllers(controllers: List<Any>): StandaloneMvcContextEnvironment {
            return StandaloneMvcContextEnvironment().controllers(controllers)
        }
    }
}