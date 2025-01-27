package io.github.bgmsound.documentify.mvc.emitter

import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.test.web.servlet.MockMvc

object EmitterFactory {
    fun createMvcEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec,
        mockMvc: MockMvc
    ): MvcDocumentEmitter {
        return RestAssuredMvcDocumentEmitter(provider, documentSpec, mockMvc)
    }
}