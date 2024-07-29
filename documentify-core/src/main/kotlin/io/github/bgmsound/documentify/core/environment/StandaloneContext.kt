package io.github.bgmsound.documentify.core.environment

import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.method.support.HandlerMethodArgumentResolver

class StandaloneContext private constructor() {
    private var controllers: List<Any> = listOf()
    private var controllerAdvices: List<Any> = listOf()
    private var argumentResolvers: List<HandlerMethodArgumentResolver> = listOf()

    fun controller(controller: Any): StandaloneContext {
        this.controllers.plus(controller)
        return this
    }

    fun controllers(vararg controllers: Any): StandaloneContext {
        this.controllers.plus(controllers)
        return this
    }

    fun controllers(controllers: List<Any>): StandaloneContext {
        this.controllers.plus(controllers)
        return this
    }

    fun controllerAdvice(controllerAdvice: Any): StandaloneContext {
        this.controllerAdvices.plus(controllerAdvice)
        return this
    }

    fun controllerAdvices(vararg controllerAdvices: Any): StandaloneContext {
        this.controllerAdvices.plus(controllerAdvices)
        return this
    }

    fun controllerAdvices(controllerAdvices: List<Any>): StandaloneContext {
        this.controllerAdvices.plus(controllerAdvices)
        return this
    }

    fun argumentResolver(argumentResolver: HandlerMethodArgumentResolver): StandaloneContext {
        this.argumentResolvers.plus(argumentResolver)
        return this
    }

    fun argumentResolvers(vararg argumentResolvers: HandlerMethodArgumentResolver): StandaloneContext {
        this.argumentResolvers.plus(argumentResolvers)
        return this
    }

    fun argumentResolvers(argumentResolvers: List<HandlerMethodArgumentResolver>): StandaloneContext {
        this.argumentResolvers.plus(argumentResolvers)
        return this
    }

    companion object {
        fun controller(controller: Any): StandaloneContext {
            return StandaloneContext().controller(controller)
        }

        fun controllers(vararg controllers: Any): StandaloneContext {
            return StandaloneContext().controllers(*controllers)
        }

        fun controllers(controllers: List<Any>): StandaloneContext {
            return StandaloneContext().controllers(controllers)
        }
    }

    fun build(context: RestDocumentationContextProvider): MockMvc {
        return MockMvcBuilders
            .standaloneSetup(*controllers.toTypedArray())
            .setControllerAdvice(*controllerAdvices.toTypedArray())
            .setCustomArgumentResolvers(*argumentResolvers.toTypedArray())
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(context))
            .build()
    }
}