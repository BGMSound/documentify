package io.github.bgmsound.documentify.core.environment

import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.method.support.HandlerMethodArgumentResolver

class StandaloneContext private constructor() {
    private val controllers: MutableList<Any> = mutableListOf()
    private val controllerAdvices: MutableList<Any> = mutableListOf()
    private val argumentResolvers: MutableList<HandlerMethodArgumentResolver> = mutableListOf()

    fun controller(controller: Any): StandaloneContext {
        this.controllers.add(controller)
        return this
    }

    fun controllers(vararg controllers: Any): StandaloneContext {
        this.controllers.addAll(controllers)
        return this
    }

    fun controllers(controllers: List<Any>): StandaloneContext {
        this.controllers.addAll(controllers)
        return this
    }

    fun controllerAdvice(controllerAdvice: Any): StandaloneContext {
        this.controllerAdvices.add(controllerAdvice)
        return this
    }

    fun controllerAdvices(vararg controllerAdvices: Any): StandaloneContext {
        this.controllerAdvices.addAll(controllerAdvices)
        return this
    }

    fun controllerAdvices(controllerAdvices: List<Any>): StandaloneContext {
        this.controllerAdvices.addAll(controllerAdvices)
        return this
    }

    fun argumentResolver(argumentResolver: HandlerMethodArgumentResolver): StandaloneContext {
        this.argumentResolvers.add(argumentResolver)
        return this
    }

    fun argumentResolvers(vararg argumentResolvers: HandlerMethodArgumentResolver): StandaloneContext {
        this.argumentResolvers.addAll(argumentResolvers)
        return this
    }

    fun argumentResolvers(argumentResolvers: List<HandlerMethodArgumentResolver>): StandaloneContext {
        this.argumentResolvers.addAll(argumentResolvers)
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