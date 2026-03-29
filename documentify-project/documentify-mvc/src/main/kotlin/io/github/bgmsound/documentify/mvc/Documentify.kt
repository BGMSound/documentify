package io.github.bgmsound.documentify.mvc

import io.github.bgmsound.documentify.core.AbstractDocumentify
import io.github.bgmsound.documentify.mvc.environment.MockMvcContextEnvironment.Companion.mockMvcEnvironment
import io.github.bgmsound.documentify.mvc.environment.StandaloneMvcContextEnvironment
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.web.servlet.MockMvc

@ExtendWith(RestDocumentationExtension::class)
abstract class Documentify : AbstractDocumentify() {
    fun mockMvc(
        provider: RestDocumentationContextProvider,
        mockMvc: MockMvc
    ) {
        this.provider = provider
        environment = mockMvcEnvironment(provider, mockMvc)
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        standaloneContext: StandaloneMvcContextEnvironment
    ) {
        this.provider = provider
        environment = standaloneContext
    }

    fun standalone(
        provider: RestDocumentationContextProvider,
        contextCustomizer: StandaloneMvcContextEnvironment.() -> Unit
    ) {
        val standaloneContext = StandaloneMvcContextEnvironment
            .standaloneEnvironment(provider)
            .also(contextCustomizer)
        standalone(provider, standaloneContext)
    }
}