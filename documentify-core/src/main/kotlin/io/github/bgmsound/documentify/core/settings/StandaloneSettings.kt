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

    fun controllers(vararg controllers: Any): StandaloneSettings {
        this.controllers = controllers.toList()
        return this
    }

    fun controllers(controllers: List<Any>): StandaloneSettings {
        this.controllers = controllers
        return this
    }

    fun controllerAdvices(vararg controllerAdvices: Any): StandaloneSettings {
        this.controllerAdvices = controllerAdvices.toList()
        return this
    }

    fun controllerAdvices(controllerAdvices: List<Any>): StandaloneSettings {
        this.controllerAdvices = controllerAdvices
        return this
    }

    fun argumentResolvers(vararg argumentResolvers: HandlerMethodArgumentResolver): StandaloneSettings {
        this.argumentResolvers = argumentResolvers.toList()
        return this
    }

    fun argumentResolvers(argumentResolvers: List<HandlerMethodArgumentResolver>): StandaloneSettings {
        this.argumentResolvers = argumentResolvers
        return this
    }

    companion object {
        fun settings(): StandaloneSettings {
            return StandaloneSettings()
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