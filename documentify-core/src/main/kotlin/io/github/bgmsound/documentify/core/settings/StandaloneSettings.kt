package io.github.bgmsound.documentify.core.settings

import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.method.support.HandlerMethodArgumentResolver

class StandaloneSettings private constructor() {
    private var controllers: List<Any> = listOf()
    private var controllerAdvices: List<Any> = listOf()
    private var argumentResolvers: List<HandlerMethodArgumentResolver> = listOf()

    fun controller(controller: Any): StandaloneSettings {
        this.controllers.plus(controller)
        return this
    }

    fun controllers(vararg controllers: Any): StandaloneSettings {
        this.controllers.plus(controllers)
        return this
    }

    fun controllers(controllers: List<Any>): StandaloneSettings {
        this.controllers.plus(controllers)
        return this
    }

    fun controllerAdvice(controllerAdvice: Any): StandaloneSettings {
        this.controllerAdvices.plus(controllerAdvice)
        return this
    }

    fun controllerAdvices(vararg controllerAdvices: Any): StandaloneSettings {
        this.controllerAdvices.plus(controllerAdvices)
        return this
    }

    fun controllerAdvices(controllerAdvices: List<Any>): StandaloneSettings {
        this.controllerAdvices.plus(controllerAdvices)
        return this
    }

    fun argumentResolver(argumentResolver: HandlerMethodArgumentResolver): StandaloneSettings {
        this.argumentResolvers.plus(argumentResolver)
        return this
    }

    fun argumentResolvers(vararg argumentResolvers: HandlerMethodArgumentResolver): StandaloneSettings {
        this.argumentResolvers.plus(argumentResolvers)
        return this
    }

    fun argumentResolvers(argumentResolvers: List<HandlerMethodArgumentResolver>): StandaloneSettings {
        this.argumentResolvers.plus(argumentResolvers)
        return this
    }

    companion object {
        fun controller(controller: Any): StandaloneSettings {
            return StandaloneSettings().controller(controller)
        }

        fun controllers(vararg controllers: Any): StandaloneSettings {
            return StandaloneSettings().controllers(*controllers)
        }

        fun controllers(controllers: List<Any>): StandaloneSettings {
            return StandaloneSettings().controllers(controllers)
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