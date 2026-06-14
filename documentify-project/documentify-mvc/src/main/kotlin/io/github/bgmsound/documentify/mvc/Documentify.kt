package io.github.bgmsound.documentify.mvc

import io.github.bgmsound.documentify.core.DocumentifySupport
import io.github.bgmsound.documentify.mvc.environment.StandaloneMvcContextEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.test.web.servlet.MockMvc

interface Documentify : DocumentifySupport {

    fun mockMvc(provider: RestDocumentationContextProvider, mockMvc: MockMvc)

    fun standalone(provider: RestDocumentationContextProvider, standaloneContext: StandaloneMvcContextEnvironment)

    fun standalone(provider: RestDocumentationContextProvider, contextCustomizer: StandaloneMvcContextEnvironment.() -> Unit)

    companion object {

        fun new(): Documentify = MvcDocumentify()
    }

}