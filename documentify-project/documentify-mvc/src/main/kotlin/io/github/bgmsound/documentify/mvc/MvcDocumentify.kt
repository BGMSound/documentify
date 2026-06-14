package io.github.bgmsound.documentify.mvc

import io.github.bgmsound.documentify.core.AbstractDocumentifySupport
import io.github.bgmsound.documentify.mvc.environment.MockMvcContextEnvironment.Companion.mockMvcEnvironment
import io.github.bgmsound.documentify.mvc.environment.StandaloneMvcContextEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.test.web.servlet.MockMvc

internal class MvcDocumentify : AbstractDocumentifySupport(), Documentify {
    override fun mockMvc(
        provider: RestDocumentationContextProvider,
        mockMvc: MockMvc
    ) {
        this.provider = provider
        environment = mockMvcEnvironment(provider, mockMvc)
    }

    override fun standalone(
        provider: RestDocumentationContextProvider,
        standaloneContext: StandaloneMvcContextEnvironment
    ) {
        this.provider = provider
        environment = standaloneContext
    }

    override fun standalone(
        provider: RestDocumentationContextProvider,
        contextCustomizer: StandaloneMvcContextEnvironment.() -> Unit
    ) {
        val standaloneContext = StandaloneMvcContextEnvironment
            .standaloneEnvironment(provider)
            .also(contextCustomizer)
        standalone(provider, standaloneContext)
    }
}