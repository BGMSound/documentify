package io.github.bgmsound.documentify.mvc.environment

import io.github.bgmsound.documentify.core.environment.AbstractDocumentContextEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.MockMvcWebTestClient

class MockMvcContextEnvironment private constructor(
    private val provider: RestDocumentationContextProvider,
    private val mockMvc: MockMvc
): AbstractDocumentContextEnvironment() {
    override fun buildTestClient(): WebTestClient {
        return MockMvcWebTestClient
            .bindTo(mockMvc)
            .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
            .build()
    }

    companion object {
        fun mockMvcEnvironment(
            provider: RestDocumentationContextProvider,
            mockMvc: MockMvc
        ): MockMvcContextEnvironment {
            return MockMvcContextEnvironment(provider, mockMvc)
        }
    }
}